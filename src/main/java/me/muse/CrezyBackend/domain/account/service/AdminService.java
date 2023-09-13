package me.muse.CrezyBackend.domain.account.service;

import me.muse.CrezyBackend.domain.account.controller.form.AdminAccountListForm;
import me.muse.CrezyBackend.domain.account.controller.form.todayStatusAccountResponseForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface AdminService {
    todayStatusAccountResponseForm todayStatusAccount(HttpHeaders headers, String date);
    List<AdminAccountListForm> accountList(HttpHeaders headers, Integer page);
    Integer getTotalPage();
    List<AdminAccountListForm> accountBlacklist(HttpHeaders headers, Integer page);
}
