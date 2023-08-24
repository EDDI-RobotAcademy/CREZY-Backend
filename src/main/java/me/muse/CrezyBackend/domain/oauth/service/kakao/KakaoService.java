package me.muse.CrezyBackend.domain.oauth.service.kakao;

import me.muse.CrezyBackend.domain.account.entity.Account;
import org.springframework.http.ResponseEntity;

public interface KakaoService {
    String kakaoLoginAddress();
    Account getAccount(String code);
}
