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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
    final private Integer weeks = 6;

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

        Integer afterDay = compareDate(TransformToDate.transformToDate(date));
        Integer previousDay = weeks-afterDay;

        LocalDate previousDate = (TransformToDate.transformToDate(date)).minusDays(previousDay);
        LocalDate afterDate = (TransformToDate.transformToDate(date)).plusDays(afterDay);

        List<Integer> accountCounts = accountListBetweenPeriod(previousDate, afterDate);
        List<String> accountDateList = accountDateListBetweenPeriod(previousDate, afterDate);

        return new todayStatusAccountResponseForm(todayAccount, totalAccount, (int)increaseRate, accountCounts, accountDateList);
    }

    public Integer compareDate(LocalDate compareDate) {
        Long date = System.currentTimeMillis();

        SimpleDateFormat sdt = new SimpleDateFormat();
        sdt.applyPattern("yyyy-MM-dd");
        String currentDate = sdt.format(date);

        LocalDate transformCurrentDate = TransformToDate.transformToDate(currentDate);

        LocalDate date1 = transformCurrentDate;
        LocalDate date2 = compareDate;

        Period period = date2.until(date1);

        int days = period.getDays();
        return days;
    }

    public List<Integer> accountListBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<Integer> accountCounts = new ArrayList<>();
        LocalDate currentDate = previousDate;

        while (!currentDate.isAfter(afterDate)) {
            AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
            Integer accounts = accountRepository.findByCreateDateAndAccountRoleType(currentDate,roleType);
            accountCounts.add(accounts);

            currentDate = currentDate.plusDays(1);
        }
        return accountCounts;
    }
    public List<String> accountDateListBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<String> accountDateList = new ArrayList<>();
        LocalDate currentDate = previousDate;

        while (!currentDate.isAfter(afterDate)) {
            accountDateList.add(currentDate.toString());

            currentDate = currentDate.plusDays(1);
        }
        log.info(accountDateList.toString());
        return accountDateList;
    }

}
