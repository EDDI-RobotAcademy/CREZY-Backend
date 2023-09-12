package me.muse.CrezyBackend.domain.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.controller.form.todayStatusAccountResponseForm;
import me.muse.CrezyBackend.domain.account.service.AdminService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    final private AdminService adminService;
    @GetMapping("/check-account")
    public todayStatusAccountResponseForm todayStatusAccount(@RequestHeader HttpHeaders headers, @RequestParam("date") String date) {
        log.info("statusTodayAccount()");
        return adminService.todayStatusAccount(headers, date);
    }
}
