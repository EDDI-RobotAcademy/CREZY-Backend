package me.muse.CrezyBackend.domain.oauth.service.google;

import me.muse.CrezyBackend.domain.account.entity.Account;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    String gooleLoginAddress();
    Account getAccount(String code);
}
