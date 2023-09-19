package me.muse.CrezyBackend.domain.oauth.service.google;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import me.muse.CrezyBackend.domain.oauth.dto.GoogleOAuthToken;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    String googleLoginAddress();
    LoginResponseForm getAccount();
    LoginResponseForm getNewAccount(LoginRequestForm requestForm);
    String checkDuplicateAccount(String code);

}
