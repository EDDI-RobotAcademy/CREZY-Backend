package me.muse.CrezyBackend.domain.oauth.service.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
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

@Service
@RequiredArgsConstructor
@PropertySource("classpath:naver.properties")
public class NaverServiceImpl implements NaverService{

    final private RedisService redisService;
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

    @Override
    public String naverLoginAddress() {
        String reqUrl = naverLoginUrl + "?response_type=code&client_id=" + naverClientId + "&redirect_uri=" + naverRedirect_uri + "&state=" + generateState();

        System.out.println(reqUrl);
        return reqUrl;
    }

    @Override
    public LoginResponseForm getAccount(String code) {
        NaverOAuthToken naverOAuthToken = getAccessToken(code);
        ResponseEntity<String> response = requestUserInfo(naverOAuthToken);
        Account account = saveUserInfo(response);

        final String userToken = UUID.randomUUID().toString();
        redisService.setKeyAndValue(userToken, account.getAccountId());
        return new LoginResponseForm(account.getNickname(), userToken);
    }

    private Account saveUserInfo(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(response.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
        Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");

        String email = (String) responseMap.get("email");
        String profileImageName = (String) responseMap.get("profile_image");
        Optional<Account> maybeAccount = accountRepository.findByEmail(email);
        Account savedAccount;
        if (maybeAccount.isEmpty()) {
            String nickname = (String) responseMap.get("nickname");
            String decodedNickname = StringEscapeUtils.unescapeJava(nickname);

            savedAccount = accountRepository.save(new Account(decodedNickname, email, profileImageName));
        } else {
            savedAccount = maybeAccount.get();
        }
        return savedAccount;
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
        return new BigInteger(130, random).toString(32);    }
}
