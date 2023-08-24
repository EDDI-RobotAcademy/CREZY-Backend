package me.muse.CrezyBackend.domain.account.service;

public interface AccountService {
    void logout(String userToken);
    Boolean checkNickname(String nickname);
    String changeNickname(String userToken, String nickname);
}
