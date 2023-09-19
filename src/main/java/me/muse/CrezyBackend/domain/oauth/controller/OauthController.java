package me.muse.CrezyBackend.domain.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
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
        return googleService.googleLoginAddress();
    }

    @GetMapping("/google-check-exist")
    public String googleCheckExist(@RequestParam String code) {
        return googleService.checkDuplicateAccount(code);
    }


    @PostMapping("/google-new-login")
    public LoginResponseForm googleCallbackNewAccount(@RequestBody LoginRequestForm requestForm) {
        return googleService.getNewAccount(requestForm);
    }

    @GetMapping("/google-login")
    public LoginResponseForm googleCallback() {
        return googleService.getAccount();
    }

    @GetMapping("/kakao")
    public String getKakaoOAuthUrl() {
        log.info("getKakaoOAuthUrl()");
        return kakaoService.kakaoLoginAddress();
    }
    @GetMapping("/kakao-check-exist")
    public String kakaoCheckExist(@RequestParam String code) {
        return kakaoService.checkDuplicateAccount(code);
    }

    @PostMapping("/kakao-new-login")
    public LoginResponseForm kakaoCallbackNewAccount(@RequestBody LoginRequestForm requestForm) {
        return kakaoService.getNewAccount(requestForm);
    }

    @GetMapping("/kakao-login")
    public LoginResponseForm kakaoCallback() {
        log.info("kakaoCallback()");
        return kakaoService.getAccount();
    }

    @GetMapping("/naver")
    public String getNaverOAuthUrl() {
        log.info("getNaverOAuthUrl()");
        return naverService.naverLoginAddress();
    }

    @GetMapping("/naver-check-exist")
    public String naverCheckExist(@RequestParam String code) {
        return naverService.checkDuplicateAccount(code);
    }

    @PostMapping("/naver-new-login")
    public LoginResponseForm naverCallbackNewAccount(@RequestBody LoginRequestForm requestForm) {
        log.info("naverCallback()");
        return naverService.getNewAccount(requestForm);
    }

    @GetMapping("/naver-login")
    public LoginResponseForm naverCallback() {
        log.info("naverCallback()");
        return naverService.getAccount();
    }
}
