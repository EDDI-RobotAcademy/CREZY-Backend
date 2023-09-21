package me.muse.CrezyBackend.domain.admin.accountManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryListResponseForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.*;
import me.muse.CrezyBackend.domain.admin.accountManage.service.AdminAccountService;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSelectListForm;
import org.springframework.data.domain.Page;
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

    @PostMapping("/account-warningCount-list")
    public Page<AdminAccountListForm> accountWarningCountList(@RequestHeader HttpHeaders headers, @RequestBody AdminAccountListRequestForm adminAccountListRequestForm) {
        log.info("accountWarningCountList()");
        return adminService.accountWarningCountList(headers, adminAccountListRequestForm);
    }

    @GetMapping("/account-detail")
    public AdminAccountDetailForm accountDetail(@RequestHeader HttpHeaders headers, @RequestParam("accountId") Long accountId) {
        log.info("accountDetail()");
        return adminService.accountDetail(headers, accountId);
    }

    @GetMapping("/change-nickname")
    public void changeNickname(@RequestHeader HttpHeaders headers, @RequestParam("accountId") Long accountId) {
        adminService.changeBadNickname(headers, accountId);
    }

    @GetMapping("/account-change-RoleType-blacklist")
    public void accountChangeRoleTypeToBlacklist(@RequestHeader HttpHeaders headers, @RequestParam("accountId") Long accountId) {
        log.info("accountChangeRoleTypeToBlacklist()");
        adminService.accountChangeRoleTypeToBlacklist(headers, accountId);
    }

    @GetMapping("/account-change-RoleType-normal")
    public void accountChangeRoleTypeToNormal(@RequestHeader HttpHeaders headers, @RequestParam("accountId") Long accountId) {
        log.info("accountChangeRoleTypeToNormal()");
        adminService.accountChangeRoleTypeToNormal(headers, accountId);
    }

    @PostMapping("/account-playlist")
    public Page<AdminPlaylistSelectListForm> playlistFindByAccount(@RequestHeader HttpHeaders headers, @RequestBody AdminPlaylistFindByAccountRequestForm requestForm) {
        log.info("playlistFindByAccount()");
        return adminService.playlistFindByAccount(headers, requestForm);
    }

    @PostMapping("/account-inquiry")
    public Page<AdminInquiryListResponseForm> inquiryFindByAccount(@RequestHeader HttpHeaders headers, @RequestBody AdminPlaylistFindByAccountRequestForm requestForm) {
        log.info("inquiryFindByAccount()");
        return adminService.inquiryFindByAccount(headers, requestForm);
    }

    @PostMapping("/search-account")
    public Page<AdminAccountListForm> searchAccount(@RequestHeader HttpHeaders headers, @RequestBody AdminAccountSearchRequestForm requestForm) {
        log.info("searchAccount()");
        return adminService.searchAccount(headers, requestForm);
    }
}
