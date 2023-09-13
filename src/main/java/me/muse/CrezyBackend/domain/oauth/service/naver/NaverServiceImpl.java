package me.muse.CrezyBackend.domain.oauth.service.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.*;
import me.muse.CrezyBackend.domain.account.repository.AccountLoginTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import me.muse.CrezyBackend.domain.oauth.dto.GoogleOAuthToken;
import me.muse.CrezyBackend.domain.oauth.dto.KakaoOAuthToken;
import me.muse.CrezyBackend.domain.oauth.dto.NaverOAuthToken;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static me.muse.CrezyBackend.domain.account.entity.LoginType.*;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.NORMAL;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:naver.properties")
public class NaverServiceImpl implements NaverService{

    final private AccountLoginTypeRepository accountLoginTypeRepository;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    final private RedisService redisService;
    final private ProfileRepository profileRepository;
    final private AccountRepository accountRepository;

    @Value("${naver.naverLoginUrl}")
    private String naverLoginUrl;
    @Value("${naver.client-id}")
    private String naverClientId;
    @Value("${naver.redirect-uri}")
    private String naverRedirect_uri;
    @Value("${naver.client-secret}")
    private String naverClientSecret;
    @Value("${naver.NAVER_TOKEN_REQUEST_URL}")
    private String NAVER_TOKEN_REQUEST_URL;
    @Value("${naver.NAVER_USERINFO_REQUEST_URL}")
    private String NAVER_USERINFO_REQUEST_URL;

    private String refreshToken;

    @Override
    public String naverLoginAddress() {
        String reqUrl = naverLoginUrl + "?response_type=code&client_id=" + naverClientId + "&redirect_uri=" + naverRedirect_uri + "&state=" + generateState();

        System.out.println(reqUrl);
        return reqUrl;
    }

    @Override
    public LoginResponseForm getAccount() {
        NaverOAuthToken naverOAuthToken = getAccessTokenFromRefreshToken();
        ResponseEntity<String> response = requestUserInfo(naverOAuthToken);

        AccountLoginType loginType = accountLoginTypeRepository.findByLoginType(NAVER).get();
        Profile profile = profileRepository.findByEmailAndAccount_LoginType(findEmail(response), loginType)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Account account = profile.getAccount();
        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        account.setLastLoginDate(null);
        accountRepository.save(account);
        return new LoginResponseForm(profile.getNickname(), userToken, profile.getProfileImageName());
    }

    @Override
    public LoginResponseForm getNewAccount(LoginRequestForm requestForm) {
        NaverOAuthToken naverOAuthToken = getAccessTokenFromRefreshToken();
        ResponseEntity<String> response = requestUserInfo(naverOAuthToken);

        Account account = saveUserInfo(response, requestForm.getNickname(), requestForm.getProfileImageName());

        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));;

        return new LoginResponseForm(profile.getNickname(), userToken, profile.getProfileImageName());
    }

    private Account saveUserInfo(ResponseEntity<String> response, String nickname, String profileImageName) {
        AccountLoginType loginType = accountLoginTypeRepository.findByLoginType(NAVER).get();
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
        Profile profile = profileRepository.save(new Profile(nickname, findEmail(response), profileImageName, new Account(loginType, roleType)));

        return profile.getAccount();
    }

    private NaverOAuthToken getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);
        body.add("redirect_uri", naverRedirect_uri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<NaverOAuthToken> response = restTemplate.exchange(
                NAVER_TOKEN_REQUEST_URL,
                HttpMethod.POST,
                requestEntity,
                NaverOAuthToken.class
        );

        return response.getBody();
    }

    private ResponseEntity<String> requestUserInfo(NaverOAuthToken naverOAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + naverOAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(NAVER_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    private String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public String findEmail(ResponseEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(response.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
        Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");

        String email = (String) responseMap.get("email");

        return email;
    }

    public boolean isExistAccount(ResponseEntity<String> response) {
        AccountLoginType loginType = accountLoginTypeRepository.findByLoginType(NAVER).get();
        Optional<Profile> maybeProfile = profileRepository.findByEmailAndAccount_LoginType(findEmail(response), loginType);

        return (maybeProfile.isPresent());
    }

    public boolean checkDuplicateAccount(String code) {
        NaverOAuthToken naverOAuthToken = getAccessToken(code);
        refreshToken = naverOAuthToken.getRefresh_token();
        ResponseEntity<String> response = requestUserInfo(naverOAuthToken);

        return isExistAccount(response);
    }

    private NaverOAuthToken getAccessTokenFromRefreshToken(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Host", "oauth2.googleapis.com");
        headers.add("Content-type", "application/x-www-form-urlencoded");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<NaverOAuthToken> response = restTemplate.exchange(NAVER_TOKEN_REQUEST_URL, HttpMethod.POST, tokenRequest, NaverOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }
}
