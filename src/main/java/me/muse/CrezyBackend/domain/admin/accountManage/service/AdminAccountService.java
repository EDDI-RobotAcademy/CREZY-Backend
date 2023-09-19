package me.muse.CrezyBackend.domain.admin.accountManage.service;

import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryListResponseForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.*;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSelectListForm;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface AdminAccountService {
    todayStatusAccountResponseForm todayStatusAccount(HttpHeaders headers, String date);
    List<AdminAccountListForm> accountList(HttpHeaders headers, Integer page);
    Integer getTotalPage();
    List<AdminAccountListForm> accountBlacklist(HttpHeaders headers, Integer page);
    AdminAccountDetailForm accountDetail(HttpHeaders headers, Long accountId);
    Integer getBlacklistTotalPage();
    Page<AdminAccountListForm> accountWarningCountList(HttpHeaders headers, AdminAccountListRequestForm adminAccountListRequestForm);
    void changeBadNickname(HttpHeaders headers, Long accountId);
    void accountChangeRoleTypeToBlacklist(HttpHeaders headers, Long accountId);
    void accountChangeRoleTypeToNormal(HttpHeaders headers, Long accountId);
    Page<AdminPlaylistSelectListForm> playlistFindByAccount(HttpHeaders headers, AdminPlaylistFindByAccountRequestForm requestForm);
    Page<AdminInquiryListResponseForm> inquiryFindByAccount(HttpHeaders headers, AdminPlaylistFindByAccountRequestForm requestForm);
}
