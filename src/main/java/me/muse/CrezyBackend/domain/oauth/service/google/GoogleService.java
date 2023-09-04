package me.muse.CrezyBackend.domain.oauth.service.google;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    String googleLoginAddress();
    LoginResponseForm getAccount(String code, LoginRequestForm requestForm);

    boolean checkDuplicateAccount(String code);
}
