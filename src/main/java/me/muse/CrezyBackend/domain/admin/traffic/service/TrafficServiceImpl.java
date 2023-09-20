package me.muse.CrezyBackend.domain.admin.traffic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.TodayTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.WeekTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.WeeklyRegistResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.entity.Traffic;
import me.muse.CrezyBackend.domain.admin.traffic.repository.TrafficRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.utility.checkAdmin.CheckAdmin;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficServiceImpl implements TrafficService{
    final private AccountRepository accountRepository;
    final private TrafficRepository trafficRepository;
    final private PlaylistRepository playlistRepository;
    final private SongRepository songRepository;
    final private CheckAdmin checkAdmin;

    @Override
    public void analysisCounting(){
        Optional<Traffic> maybeTraffic = trafficRepository.findByDate(LocalDate.now());
        Traffic traffic;

        if(maybeTraffic.isEmpty()) {
            traffic = new Traffic(LocalDate.now());
            traffic.setAnalysisCount(1);
        }else {
            traffic = maybeTraffic.get();
            traffic.setAnalysisCount(traffic.getAnalysisCount()+1);
        }

        trafficRepository.save(traffic);
    }

    @Override
    public void loginCounting(){
        Optional<Traffic> maybeTraffic = trafficRepository.findByDate(LocalDate.now());
        Traffic traffic;

        if(maybeTraffic.isEmpty()) {
            traffic = new Traffic(LocalDate.now());
            traffic.setLoginCount(1);
        }else {
            traffic = maybeTraffic.get();
            traffic.setLoginCount(traffic.getLoginCount()+1);
        }
        trafficRepository.save(traffic);
    }

    @Override
    public TodayTrafficCountResponseForm todayCount(HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        Traffic traffic = trafficRepository.findByDate(LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("Traffic not found"));

        return new TodayTrafficCountResponseForm(traffic.getLoginCount(), traffic.getAnalysisCount());
    }

    @Override
    public WeekTrafficCountResponseForm weekCount(int weekValue, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        LocalDate now = LocalDate.now().minusDays(7L * weekValue);
        LocalDate monday = now.with(DayOfWeek.MONDAY);
        LocalDate sunday = now.with(DayOfWeek.SUNDAY);

        List<LocalDate> dateList = new ArrayList<>();

        for (LocalDate date = monday; !date.isAfter(sunday); date = date.plusDays(1)) {
            dateList.add(date);
        }
        List<Integer> loginCountList = new ArrayList<>();
        List<Integer> analysisCountList = new ArrayList<>();

        for (LocalDate date : dateList){
            Optional<Traffic> maybeTraffic = trafficRepository.findByDate(date);
            if(maybeTraffic.isEmpty()){
                loginCountList.add(0);
                analysisCountList.add(0);
            }else {
                Traffic traffic = maybeTraffic.get();
                loginCountList.add(traffic.getLoginCount());
                analysisCountList.add(traffic.getAnalysisCount());
            }
        }

        return new WeekTrafficCountResponseForm(loginCountList, analysisCountList);
    }

    @Override
    public List<WeeklyRegistResponseForm> weeklyRegist(int weekValue, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        LocalDate now = LocalDate.now().minusDays(7L * weekValue);
        LocalDate monday = now.with(DayOfWeek.MONDAY);
        LocalDate sunday = now.with(DayOfWeek.SUNDAY);

        List<LocalDate> dateList = new ArrayList<>();
        List<WeeklyRegistResponseForm> responseFormList = new ArrayList<>();

        for (LocalDate date = monday; !date.isAfter(sunday); date = date.plusDays(1)) {
            dateList.add(date);
        }

        for (LocalDate date : dateList){
            List<Account> accountList = accountRepository.findByCreateDate(date);
            List<Playlist> playlists = playlistRepository.findByCreateDate(date);
            List<Song> songList = songRepository.findByCreateDate(date);

            WeeklyRegistResponseForm responseForm = new WeeklyRegistResponseForm(accountList.size(), playlists.size(), songList.size());
            responseFormList.add(responseForm);
        }

        return responseFormList;
    }
}
