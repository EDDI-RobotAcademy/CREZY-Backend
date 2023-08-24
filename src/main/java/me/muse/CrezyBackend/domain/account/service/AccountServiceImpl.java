package me.muse.CrezyBackend.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    final private RedisService redisService;
    final private AccountRepository accountRepository;
    @Override
    public void logout(String userToken) {
        redisService.deleteByKey(userToken);
    }
    @Override
    public Boolean checkNickname(String nickname) {
        final Optional<Account> maybeAccount = accountRepository.findByNickname(nickname);
        if (maybeAccount.isPresent()) {
            return false;
        }
        return true;
    }
}
