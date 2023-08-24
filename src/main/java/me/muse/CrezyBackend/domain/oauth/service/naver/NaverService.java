package me.muse.CrezyBackend.domain.oauth.service.naver;

import me.muse.CrezyBackend.domain.account.entity.Account;

public interface NaverService {
    String naverLoginAddress();
    Account getAccount(String code);
}
