package me.muse.CrezyBackend.domain.oauth.service.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.*;
import me.muse.CrezyBackend.domain.account.repository.AccountLoginTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.traffic.service.TrafficService;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import me.muse.CrezyBackend.domain.oauth.dto.KakaoOAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static me.muse.CrezyBackend.domain.account.entity.LoginType.KAKAO;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.BLACKLIST;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.NORMAL;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:kakao.properties")
public class KakaoServiceImpl implements KakaoService{

    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private AccountLoginTypeRepository accountLoginTypeRepository;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    final private ProfileRepository profileRepository;
    final private TrafficService trafficService;
    @Value("${kakao.kakaoLoginUrl}")
    private String kakaoLoginUrl;
    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirect_uri;
    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${kakao.KAKAO_TOKEN_REQUEST_URL}")
    private String KAKAO_TOKEN_REQUEST_URL;
    @Value("${kakao.KAKAO_USERINFO_REQUEST_URL}")
    private String KAKAO_USERINFO_REQUEST_URL;
    private String refreshToken;


    @Override
    public String kakaoLoginAddress() {
        String reqUrl = kakaoLoginUrl + "/oauth/authorize?client_id=" + kakaoClientId + "&redirect_uri=" + kakaoRedirect_uri
                + "&response_type=code";
        // 카카오 재로그인 해야할 때 url뒤에 붙이기: + "&prompt=login"
        System.out.println(reqUrl);
        return reqUrl;
    }

    @Override
    public String checkDuplicateAccount(String code) {
        KakaoOAuthToken kakaoOAuthToken = getAccessToken(code);
        refreshToken = kakaoOAuthToken.getRefresh_token();
        ResponseEntity<String> response = requestUserInfo(kakaoOAuthToken);
        return isExistAccount(response);
    }
    public String isExistAccount(ResponseEntity<String> response){
        AccountLoginType loginType = accountLoginTypeRepository.findByLoginType(KAKAO).get();
        Optional<Profile> maybeProfile = profileRepository.findByEmailAndAccount_LoginType(findEmail(response), loginType);

        if(maybeProfile.isEmpty()){ return "NEW"; }

        if(maybeProfile.get().getAccount().getRoleType().getRoleType()==BLACKLIST){
            return "BLACKLIST";
        } else {
            return "NORMAL";
        }
    }

    public String findEmail(ResponseEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(response.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
        Map<String, Object> kakaoAccountMap = (Map<String, Object>) jsonMap.get("kakao_account");
        String email = (String) kakaoAccountMap.get("email");

        return email;
    }

    @Override
    public LoginResponseForm getNewAccount(LoginRequestForm requestForm) {
        KakaoOAuthToken kakaoOAuthToken = getAccessTokenFromRefreshToken();
        ResponseEntity<String> response = requestUserInfo(kakaoOAuthToken);

        Account account = saveUserInfo(response, requestForm.getNickname(), requestForm.getProfileImageName());

        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        trafficService.loginCounting();

        return new LoginResponseForm(profile.getNickname(), userToken, profile.getProfileImageName());
    }

    @Override
    public LoginResponseForm getAccount() {
        KakaoOAuthToken kakaoOAuthToken = getAccessTokenFromRefreshToken();
        ResponseEntity<String> response = requestUserInfo(kakaoOAuthToken);

        AccountLoginType loginType = accountLoginTypeRepository.findByLoginType(KAKAO).get();
        Profile profile = profileRepository.findByEmailAndAccount_LoginType(findEmail(response), loginType)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Account account = profile.getAccount();

        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        account.setLastLoginDate(null);
        accountRepository.save(account);

        trafficService.loginCounting();

        return new LoginResponseForm(profile.getNickname(), userToken, profile.getProfileImageName());
    }

    private KakaoOAuthToken getAccessTokenFromRefreshToken(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoClientId);
        body.add("client_secret", kakaoClientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoOAuthToken> response = restTemplate.exchange(KAKAO_TOKEN_REQUEST_URL, HttpMethod.POST, tokenRequest, KakaoOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }
    private Account saveUserInfo(ResponseEntity<String> response, String nickname, String profileImageName) {
        AccountLoginType loginType = accountLoginTypeRepository.findByLoginType(KAKAO).get();
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
        Profile profile = profileRepository.save(new Profile(nickname, findEmail(response), profileImageName, new Account(loginType, roleType)));

        return profile.getAccount();
    }

    private ResponseEntity<String> requestUserInfo(KakaoOAuthToken kakaoOAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoOAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    private KakaoOAuthToken getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirect_uri);
        body.add("code", code);
        body.add("client_secret", kakaoClientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoOAuthToken> response = restTemplate.exchange(KAKAO_TOKEN_REQUEST_URL, HttpMethod.POST, tokenRequest, KakaoOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }
}
