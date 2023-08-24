package me.muse.CrezyBackend.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    final private RedisService redisService;
    @Override
    public void logout(String userToken) {
        redisService.deleteByKey(userToken);
    }
}
