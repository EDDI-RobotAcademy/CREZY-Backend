package me.muse.CrezyBackend.domain.admin.accountManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountDetailForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountListForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.todayStatusAccountResponseForm;
import me.muse.CrezyBackend.domain.admin.accountManage.service.AdminAccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-account")
public class AdminAccountController {
    final private AdminAccountService adminService;
    @GetMapping("/check-account")
    public todayStatusAccountResponseForm todayStatusAccount(@RequestHeader HttpHeaders headers, @RequestParam("date") String date) {
        log.info("statusTodayAccount()");
        return adminService.todayStatusAccount(headers, date);
    }

    @GetMapping("/account-list")
    public List<AdminAccountListForm> accountList(@RequestHeader HttpHeaders headers, @RequestParam("page") Integer page) {
        log.info("accountList()");
        return adminService.accountList(headers, page);
    }
    @GetMapping("/list/total-page")
    public Integer getTotalPage() {
        return adminService.getTotalPage();
    }

    @GetMapping("/account-blacklist")
    public List<AdminAccountListForm> accountBlacklist(@RequestHeader HttpHeaders headers, @RequestParam("page") Integer page) {
        log.info("accountBlacklist()");
        return adminService.accountBlacklist(headers, page);
    }
    @GetMapping("/list/blacklist-total-page")
    public Integer getBlacklistTotalPage() {
        return adminService.getBlacklistTotalPage();
    }

    @GetMapping("/account-detail")
    public AdminAccountDetailForm accountDetail(@RequestHeader HttpHeaders headers, @RequestParam("accountId") Long accountId) {
        log.info("accountDetail()");
        return adminService.accountDetail(headers, accountId);
    }
}
