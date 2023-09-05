package me.muse.CrezyBackend.domain.oauth.service.naver;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;

public interface NaverService {
    String naverLoginAddress();
    LoginResponseForm getAccount();
    LoginResponseForm getNewAccount(LoginRequestForm requestForm);
    boolean checkDuplicateAccount(String code);
}
