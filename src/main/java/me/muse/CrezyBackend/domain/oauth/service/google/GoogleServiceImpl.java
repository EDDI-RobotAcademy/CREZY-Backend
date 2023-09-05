package me.muse.CrezyBackend.domain.oauth.service.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.LoginType;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import me.muse.CrezyBackend.domain.oauth.dto.GoogleOAuthToken;
import me.muse.CrezyBackend.domain.oauth.service.google.GoogleService;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:google.properties")
public class GoogleServiceImpl implements GoogleService {
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    @Value("${google.googleLoginUrl}")
    private String googleLoginUrl;
    @Value("${google.GOOGLE_TOKEN_REQUEST_URL}")
    private String GOOGLE_TOKEN_REQUEST_URL;
    @Value("${google.GOOGLE_USERINFO_REQUEST_URL}")
    private String GOOGLE_USERINFO_REQUEST_URL;
    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.redirect-uri}")
    private String googleRedirect_uri;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.GOOGLE_ACCESSTOKEN_FROM_REFRESHTOKEN_URL}")
    private String GOOGLE_ACCESSTOKEN_FROM_REFRESHTOKEN_URL;

    private String refreshToken;
    public String googleLoginAddress(){
        String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirect_uri
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        System.out.println(reqUrl);
        return reqUrl;
    }
    private GoogleOAuthToken getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Host", "oauth2.googleapis.com");
        headers.add("Content-type", "application/x-www-form-urlencoded");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleClientId);
        body.add("redirect_uri", googleRedirect_uri);
        body.add("code", code);
        body.add("client_secret", googleClientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<GoogleOAuthToken> response = restTemplate.exchange(GOOGLE_TOKEN_REQUEST_URL, HttpMethod.POST, tokenRequest, GoogleOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }

    private GoogleOAuthToken getAccessTokenFromRefreshToken(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Host", "oauth2.googleapis.com");
        headers.add("Content-type", "application/x-www-form-urlencoded");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<GoogleOAuthToken> response = restTemplate.exchange(GOOGLE_ACCESSTOKEN_FROM_REFRESHTOKEN_URL, HttpMethod.POST, tokenRequest, GoogleOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }

    private ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }


    private Account saveUserInfo(ResponseEntity<String> response, String nickname, String profileImageName){
        return accountRepository.save(new Account(nickname, findEmail(response), LoginType.GOOGLE, profileImageName));
    }

    public boolean isExistAccount(ResponseEntity<String> response){
        Optional<Account> maybeAccount = accountRepository.findByEmail(findEmail(response));

        return (maybeAccount.isPresent() && maybeAccount.get().getLoginType().equals(LoginType.GOOGLE));
    }

    @Override
    public LoginResponseForm getAccount() {
        GoogleOAuthToken googleOAuthToken = getAccessTokenFromRefreshToken();
        ResponseEntity<String> response = requestUserInfo(googleOAuthToken);

        Account account = accountRepository.findByEmail(findEmail(response))
                .orElseThrow(() -> new IllegalArgumentException("계정 없음"));

        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        return new LoginResponseForm(account.getNickname(), userToken, account.getProfileImageName());
    }

    @Override
    public LoginResponseForm getNewAccount(LoginRequestForm requestForm) {
        GoogleOAuthToken googleOAuthToken = getAccessTokenFromRefreshToken();
        ResponseEntity<String> response = requestUserInfo(googleOAuthToken);

        Account account = saveUserInfo(response, requestForm.getNickname(), requestForm.getProfileImageName());

        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        return new LoginResponseForm(account.getNickname(), userToken, account.getProfileImageName());
    }

    @Override
    public boolean checkDuplicateAccount(String code) {
        GoogleOAuthToken googleOAuthToken = getAccessToken(code);
        refreshToken = googleOAuthToken.getRefresh_token();
        ResponseEntity<String> response = requestUserInfo(googleOAuthToken);

        return isExistAccount(response);
    }

    public String findEmail(ResponseEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(response.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
        String email = (String) jsonMap.get("email");

        return email;
    }
}
