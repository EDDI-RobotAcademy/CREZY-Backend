package me.muse.CrezyBackend.domain.oauth.service.kakao;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginRequestForm;
import me.muse.CrezyBackend.domain.oauth.controller.form.LoginResponseForm;
import org.springframework.http.ResponseEntity;

public interface KakaoService {
    String kakaoLoginAddress();
    LoginResponseForm getAccount();
    LoginResponseForm getNewAccount(LoginRequestForm requestForm);
    String checkDuplicateAccount(String code);

}
