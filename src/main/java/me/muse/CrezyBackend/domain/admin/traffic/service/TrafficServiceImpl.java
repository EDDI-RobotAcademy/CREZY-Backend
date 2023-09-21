package me.muse.CrezyBackend.domain.admin.traffic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryDetailRepository;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.TodayTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.WeekTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.WeeklyRegistResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.entity.Traffic;
import me.muse.CrezyBackend.domain.admin.traffic.repository.TrafficRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
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
    final private ReportDetailRepository reportDetailRepository;
    final private InquiryDetailRepository inquiryDetailRepository;

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

        Optional<Traffic> maybeTraffic = trafficRepository.findByDate(LocalDate.now());
        Traffic traffic;
        if(maybeTraffic.isEmpty()){
            traffic = new Traffic(LocalDate.now());
            trafficRepository.save(traffic);
        }else {
            traffic = maybeTraffic.get();
        }

        Optional<Traffic> maybePreviousTraffic = trafficRepository.findByDate(LocalDate.now().minusDays(1));
        Traffic previousTraffic;
        if(maybePreviousTraffic.isEmpty()){
            previousTraffic = new Traffic(LocalDate.now().minusDays(1));
            trafficRepository.save(previousTraffic);
        }else{
            previousTraffic = maybePreviousTraffic.get();
        }

        int loginCountRate = calculateIncreaseRate(traffic.getLoginCount(), previousTraffic.getLoginCount());
        int analysisCountRate = calculateIncreaseRate(traffic.getAnalysisCount(), previousTraffic.getAnalysisCount());

        int reportCount = reportDetailRepository.countByCreateReportDate(LocalDate.now());
        int previousReportCount = reportDetailRepository.countByCreateReportDate(LocalDate.now().minusDays(1));
        int reportCountRate = calculateIncreaseRate(reportCount,previousReportCount);

        int inquiryCount = inquiryDetailRepository.countByCreateInquiryDate(LocalDate.now());
        int previousInquiryCount = inquiryDetailRepository.countByCreateInquiryDate(LocalDate.now().minusDays(1));
        int inquiryCountRate = calculateIncreaseRate(inquiryCount,previousInquiryCount);

        return new TodayTrafficCountResponseForm(traffic.getLoginCount(), traffic.getAnalysisCount(), reportCount, inquiryCount, loginCountRate, analysisCountRate, reportCountRate, inquiryCountRate);
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

    public int calculateIncreaseRate(int todayCount,int previousCount){
        double increaseRate=0;

        if(0 < todayCount && previousCount == 0){
            increaseRate = 100;
        }else {
            increaseRate = (double) (todayCount - previousCount) / previousCount * 100;
        }
        return (int)increaseRate;
    }
}
