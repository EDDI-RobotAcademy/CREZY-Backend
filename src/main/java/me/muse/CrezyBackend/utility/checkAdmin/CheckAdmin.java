package me.muse.CrezyBackend.utility.checkAdmin;

import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;

@Service
@RequiredArgsConstructor
public class CheckAdmin {
    final private RedisService redisService;
    final private AccountRepository accountRepository;

    public boolean checkAdmin(HttpHeaders headers) {
        Long accountId;
        try{
            List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
            accountId = redisService.getValueByKey(authValues.get(0));
        }catch (NullPointerException e){
            return false;
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        return account.getRoleType().getRoleType() == ADMIN;
    }
}
