package me.muse.CrezyBackend.domain.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.service.google.GoogleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {
    final private GoogleService googleService;

    @GetMapping("/google")
    public @ResponseBody String getGoogleOAuthUrl() {
        return googleService.gooleLoginAddress();
    }

    @GetMapping("/google-login")
    public Account googleCallback(@RequestParam String code) {
        return googleService.getAccount(code);
    }

}
