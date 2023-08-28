package me.muse.CrezyBackend.domain.oauth.service.google;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    String gooleLoginAddress();
    LoginResponseForm getAccount(String code);
}
