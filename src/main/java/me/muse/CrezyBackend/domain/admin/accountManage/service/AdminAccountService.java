package me.muse.CrezyBackend.domain.admin.accountManage.service;

import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountDetailForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountListForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountListRequestForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.todayStatusAccountResponseForm;
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
}
