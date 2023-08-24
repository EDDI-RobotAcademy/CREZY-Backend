package me.muse.CrezyBackend.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.service.AccountService;
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
}
