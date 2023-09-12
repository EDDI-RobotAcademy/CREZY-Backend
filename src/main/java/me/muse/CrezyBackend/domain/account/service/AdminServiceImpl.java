package me.muse.CrezyBackend.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.controller.form.todayStatusAccountResponseForm;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.utility.TransformToDate.TransformToDate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.NORMAL;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    @Override
    public todayStatusAccountResponseForm todayStatusAccount(HttpHeaders headers, String date) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getRoleType().getRoleType() != ADMIN) {
            return null;
        }
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
        Integer todayAccount = accountRepository.findByCreateDateAndAccountRoleType(TransformToDate.transformToDate(date), roleType);

        Integer totalAccount = accountRepository.findByAccountRoleType(roleType);
        Integer previousAccount = accountRepository.findByCreateDateAndAccountRoleType((TransformToDate.transformToDate(date)).minusDays(1), roleType);
        double increaseRate =  (double)(todayAccount-previousAccount)/previousAccount * 100;
        return new todayStatusAccountResponseForm(todayAccount, totalAccount, (int)increaseRate);
    }
}
