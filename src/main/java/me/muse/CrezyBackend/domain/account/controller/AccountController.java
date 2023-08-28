package me.muse.CrezyBackend.domain.account.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    final private AccountService accountService;

    @GetMapping("/logout")
    public void logout(@RequestParam("userToken") String userToken) {
        accountService.logout(userToken);
    }
    @GetMapping("/check-nickName/{nickname}")
    public boolean checkNickname(@PathVariable("nickname") String nickname) {
        return accountService.checkNickname(nickname);
    }
    @GetMapping("/change-nickname")
    public String changeNickname(@RequestParam("userToken") String userToken, @RequestParam("nickname") String nickname) {
        return accountService.changeNickname(userToken, nickname);
    }

    @DeleteMapping("/withdraw")
    public Boolean withdrawal(@RequestHeader HttpHeaders headers){
        log.info("withdrawal()");
        return accountService.withdrawal(headers);
    }
}
