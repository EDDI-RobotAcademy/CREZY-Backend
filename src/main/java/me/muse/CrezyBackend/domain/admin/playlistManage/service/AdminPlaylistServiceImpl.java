package me.muse.CrezyBackend.domain.admin.playlistManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.todayStatusPlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminPlaylistServiceImpl implements AdminPlaylistService {
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private PlaylistRepository playlistRepository;
    final private Integer weeks = 6;

    @Override
    public todayStatusPlaylistResponseForm todayStatusPlaylist(HttpHeaders headers, String date) {
        if (checkAdmin(headers)) return null;
        Integer todayPlaylist = playlistRepository.findByCreateDate(TransformToDate.transformToDate(date)).size();
        Integer totalPlaylist = playlistRepository.findAll().size();
        Integer previousPlaylist = playlistRepository.findByCreateDate((TransformToDate.transformToDate(date)).minusDays(1)).size();
        double increaseRate = 0;
        if(0 < todayPlaylist && previousPlaylist == 0){
            increaseRate = 100;
        }else {
            increaseRate = (double) (todayPlaylist - previousPlaylist) / previousPlaylist * 100;
        }
        Integer afterDay = compareDate(TransformToDate.transformToDate(date));
        Integer previousDay = weeks-afterDay;

        LocalDate previousDate = (TransformToDate.transformToDate(date)).minusDays(previousDay);
        LocalDate afterDate = (TransformToDate.transformToDate(date)).plusDays(afterDay);

        List<Integer> playlistCounts = playlistBetweenPeriod(previousDate, afterDate);
        List<String> playlistDateList = playlistDateListBetweenPeriod(previousDate, afterDate);

        return new todayStatusPlaylistResponseForm(todayPlaylist, totalPlaylist, (int)increaseRate, playlistCounts, playlistDateList);
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
        if(days < 3) {
            return days;
        }
        return 3;
    }

    public List<Integer> playlistBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<Integer> playlistCounts = new ArrayList<>();
        while (!previousDate.isAfter(afterDate)) {
            Integer playlists = playlistRepository.findByCreateDate(previousDate).size();
            playlistCounts.add(playlists);
            previousDate = previousDate.plusDays(1);
        }
        return playlistCounts;
    }
    public List<String> playlistDateListBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<String> playlistDateList = new ArrayList<>();
        while (!previousDate.isAfter(afterDate)) {
            playlistDateList.add(previousDate.toString());
            previousDate = previousDate.plusDays(1);
        }
        return playlistDateList;
    }

    private boolean checkAdmin(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return true;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getRoleType().getRoleType() != ADMIN) {
            return true;
        }
        return false;
    }
}

