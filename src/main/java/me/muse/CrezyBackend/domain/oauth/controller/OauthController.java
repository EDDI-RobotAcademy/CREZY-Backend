package me.muse.CrezyBackend.domain.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import me.muse.CrezyBackend.domain.oauth.service.google.GoogleService;
import me.muse.CrezyBackend.domain.oauth.service.kakao.KakaoService;
import me.muse.CrezyBackend.domain.oauth.service.naver.NaverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {
    final private GoogleService googleService;
    final private KakaoService kakaoService;
    final private NaverService naverService;

    @GetMapping("/google")
    public @ResponseBody String getGoogleOAuthUrl() {
        return googleService.gooleLoginAddress();
    }

    @GetMapping("/google-login")
    public LoginResponseForm googleCallback(@RequestParam String code) {
        return googleService.getAccount(code);
    }

    @GetMapping("/kakao")
    public String getKakaoOAuthUrl() {
        log.info("getKakaoOAuthUrl()");
        return kakaoService.kakaoLoginAddress();
    }

    @GetMapping("/kakao-login")
    public LoginResponseForm kakaoCallback(@RequestParam String code) {
        log.info("kakaoCallback()");
        return kakaoService.getAccount(code);
    }

    @GetMapping("/naver")
    public String getNaverOAuthUrl() {
        log.info("getNaverOAuthUrl()");
        return naverService.naverLoginAddress();
    }

    @GetMapping("/naver-login")
    public LoginResponseForm naverCallback(@RequestParam String code) {
        log.info("naverCallback()");
        return naverService.getAccount(code);
    }
}
