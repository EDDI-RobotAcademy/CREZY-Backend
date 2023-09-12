package me.muse.CrezyBackend.domain.account.service;

import me.muse.CrezyBackend.domain.account.controller.form.todayStatusAccountResponseForm;
import org.springframework.http.HttpHeaders;

public interface AdminService {
    todayStatusAccountResponseForm todayStatusAccount(HttpHeaders headers, String date);
}
