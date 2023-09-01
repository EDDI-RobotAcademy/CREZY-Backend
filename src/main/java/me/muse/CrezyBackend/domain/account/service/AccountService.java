package me.muse.CrezyBackend.domain.account.service;

import me.muse.CrezyBackend.domain.account.controller.form.AccountInfoResponseForm;
import org.springframework.http.HttpHeaders;

public interface AccountService {
    void logout(String userToken);
    Boolean checkNickname(String nickname);
    String changeNickname(String userToken, String nickname);
    Boolean withdrawal(HttpHeaders headers);
    AccountInfoResponseForm returnAccountInfo(HttpHeaders headers);
    String changeProfileImage(HttpHeaders headers, String profileImageName);
}
