package me.muse.CrezyBackend.domain.admin.accountManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryCountResponseForm;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryDetailRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryRepository;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountDetailForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountListForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.AdminAccountListRequestForm;
import me.muse.CrezyBackend.domain.admin.accountManage.controller.form.todayStatusAccountResponseForm;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import me.muse.CrezyBackend.domain.report.entity.ReportStatus;
import me.muse.CrezyBackend.domain.report.entity.ReportStatusType;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.domain.warning.repository.WarningRepository;
import me.muse.CrezyBackend.utility.RandomValue;
import me.muse.CrezyBackend.utility.TransformToDate.TransformToDate;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAccountServiceImpl implements AdminAccountService {
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    final private ProfileRepository profileRepository;
    final private WarningRepository warningRepository;
    final private PlaylistRepository playlistRepository;
    final private SongRepository songRepository;
    final private ReportRepository reportRepository;
    final private ReportDetailRepository reportDetailRepository;
    final private LikePlaylistRepository likePlaylistRepository;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private InquiryRepository inquiryRepository;
    final private InquiryDetailRepository inquiryDetailRepository;
    final private Integer weeks = 6;

    @Override
    public todayStatusAccountResponseForm todayStatusAccount(HttpHeaders headers, String date) {
        if (checkAdmin(headers)) return null;
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
        Integer todayAccount = accountRepository.findByCreateDateAndAccountRoleType(TransformToDate.transformToDate(date), roleType);

        Integer totalAccount = accountRepository.findByAccountRoleType(roleType);
        Integer previousAccount = accountRepository.findByCreateDateAndAccountRoleType((TransformToDate.transformToDate(date)).minusDays(1), roleType);
        double increaseRate = 0;
        if(0 < todayAccount && previousAccount == 0){
            increaseRate = 100;
        }else {
            increaseRate = (double) (todayAccount - previousAccount) / previousAccount * 100;
        }
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
        if(days < 3) {
            return days;
        }
        return 3;
    }

    public List<Integer> accountListBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<Integer> accountCounts = new ArrayList<>();
        while (!previousDate.isAfter(afterDate)) {
            AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
            Integer accounts = accountRepository.findByCreateDateAndAccountRoleType(previousDate,roleType);
            accountCounts.add(accounts);
            previousDate = previousDate.plusDays(1);
        }
        return accountCounts;
    }
    public List<String> accountDateListBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<String> accountDateList = new ArrayList<>();
        while (!previousDate.isAfter(afterDate)) {
            accountDateList.add(previousDate.toString());
            previousDate = previousDate.plusDays(1);
        }
        return accountDateList;
    }
    public List<Integer> songCountsListBetweenPeriod(LocalDate previousDate, LocalDate afterDate, Account account) {
        List<Integer> songCounts = new ArrayList<>();
        List<Playlist> playlists = playlistRepository.findPlaylistIdByAccount(account);
        while (!previousDate.isAfter(afterDate)) {
            int songCount = 0;
            for (Playlist playlist : playlists) {
                songCount += songRepository.countByPlaylistAndCreateDate(playlist, previousDate);
            }
            songCounts.add(songCount);
            previousDate = previousDate.plusDays(1);
        }
        return songCounts;
    }
    public List<Integer> playlistCountsListBetweenPeriod(LocalDate previousDate, LocalDate afterDate, Account account){
        List<Integer> playlistCounts = new ArrayList<>();
        while (!previousDate.isAfter(afterDate)) {
            List<Playlist> playlists = playlistRepository.countByAccountAndCreateDate(account, previousDate);
            playlistCounts.add(playlists.size());
            previousDate = previousDate.plusDays(1);
        }
        return playlistCounts;
    }

    @Override
    public List<AdminAccountListForm> accountList(HttpHeaders headers, Integer page) {
        if (checkAdmin(headers)) return null;
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("account.createDate").descending());
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(NORMAL).get();
        List<Profile> profileList = profileRepository.findByAccount_RoleTypeWithPage(pageable, roleType);
        final List<AdminAccountListForm> adminAccountListForms = new ArrayList<>();
        for(Profile isProfile : profileList){
            Account isAccount = accountRepository.findById(isProfile.getAccount().getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("account 없음"));

            Integer playlistCounts = playlistRepository.countByAccount(isAccount);
            List<Playlist> playlists = playlistRepository.findPlaylistIdByAccount(isAccount);
            Integer songCounts = 0;
            for(Playlist playlist : playlists){
                songCounts += songRepository.countByPlaylist(playlist);
            }
            Integer warningCounts = warningRepository.countByAccount(isAccount);
            AdminAccountListForm adminAccountListForm = new AdminAccountListForm(isProfile.getAccount().getAccountId(), isProfile.getNickname(), playlistCounts, songCounts, isProfile.getAccount().getCreateDate(), warningCounts, isAccount.getRoleType().getRoleType().toString());
            adminAccountListForms.add(adminAccountListForm);
        }
        log.info(adminAccountListForms.toString());
        return adminAccountListForms;
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

    @Override
    public Integer getTotalPage() {
        Integer totalReport = (int) accountRepository.count();
        Integer size = 10;
        if (totalReport % size == 0) {
            return totalReport / size;
        } else {
            return totalReport / size + 1;
        }
    }
    @Override
    public List<AdminAccountListForm> accountBlacklist(HttpHeaders headers, Integer page) {
        if (checkAdmin(headers)) return null;
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("account.createDate").descending());
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
        List<Profile> profileList = profileRepository.findAllBlacklistWithPage(pageable, roleType);
        final List<AdminAccountListForm> adminAccountListForms = new ArrayList<>();
        for (Profile isProfile : profileList) {
            Account isAccount = accountRepository.findAccountByAccountRoleType(roleType)
                    .orElseThrow(() -> new IllegalArgumentException("account 없음"));

            List<Playlist> playlists = playlistRepository.findPlaylistIdByAccount(isAccount);
            Integer playlistCounts = playlists.size();
            Integer songCounts = 0;
            for (Playlist playlist : playlists) {
                songCounts += songRepository.countByPlaylist(playlist);
            }
            Integer warningCounts = warningRepository.countByAccount(isAccount);
            AdminAccountListForm adminAccountListForm = new AdminAccountListForm(isProfile.getAccount().getAccountId(), isProfile.getNickname(), playlistCounts, songCounts, isProfile.getAccount().getCreateDate(), warningCounts);
            adminAccountListForms.add(adminAccountListForm);
        }
        log.info(adminAccountListForms.toString());
        return adminAccountListForms;
    }

    @Override
    public Integer getBlacklistTotalPage() {
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
        Integer totalReport = accountRepository.findByAccountRoleType(roleType);
        Integer size = 10;
        if (totalReport % size == 0) {
            return totalReport / size;
        } else {
            return totalReport / size + 1;
        }
    }

    @Override
    public AdminAccountDetailForm accountDetail(HttpHeaders headers, Long accountId) {
        if (checkAdmin(headers)) return null;
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account 없음"));;
        Profile profile = profileRepository.findByAccount(account).get();
        List<ReportDetail> reportDetails = reportDetailRepository.findAllByReportedAccountId(accountId);
        Integer reportedCounts = 0;
        for(ReportDetail reportDetail : reportDetails){
            reportedCounts += reportRepository.countByReportId(reportDetail.getReport().getReportId());
        }
        List<Playlist> playlists = playlistRepository.findPlaylistIdByAccount(account);
        Integer playlistCounts = playlists.size();
        Integer songCounts = 0;
        for (Playlist playlist : playlists) {
            songCounts += songRepository.countByPlaylist(playlist);
        }
        Integer warningCounts = warningRepository.countByAccount(account);
        Integer likePlaylistCounts = likePlaylistRepository.countByAccount(account);

        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = LocalDate.now().minusDays(6);

        List<Integer> playlistCountsList = playlistCountsListBetweenPeriod(previousDate, currentDate, account);
        List<Integer> songCountsList = songCountsListBetweenPeriod(previousDate, currentDate, account);
        List<String> accountDateList = accountDateListBetweenPeriod(previousDate, currentDate);

        AdminAccountDetailForm adminAccountDetailForm = new AdminAccountDetailForm(accountId, profile.getNickname(), profile.getEmail(), warningCounts, reportedCounts, profile.getAccount().getLastLoginDate(),
                playlistCounts, songCounts, likePlaylistCounts, playlistCountsList, songCountsList, accountDateList);
        return adminAccountDetailForm;
        }
    @Override
    public Page<AdminAccountListForm> accountWarningCountList(HttpHeaders headers, AdminAccountListRequestForm requestForm) {
        if (checkAdmin(headers)) return null;
        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10, Sort.by("account.accountId").descending());
        ReportStatusType reportStatus = reportStatusTypeRepository.findByReportStatus(ReportStatus.APPROVE).get();
        List<Report> reports = reportRepository.findByReportStatusType(reportStatus);
        List<Profile> profiles = new ArrayList<>();
        List<ReportDetail> reportDetails = new ArrayList<>();
        for(Report report : reports) {
            ReportDetail details = reportDetailRepository.findByReportId(report.getReportId())
                    .orElseThrow(() -> new IllegalArgumentException("reportDetail 없음"));
            reportDetails.add(details);
        }
        for(ReportDetail reportDetail: reportDetails) {
            Profile profile = profileRepository.findByAccount_AccountId(reportDetail.getReportedAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("profile 없음"));
            profiles.add(profile);
        }
        Map<Long, Integer> accountCounts = new HashMap<>();
        for(Profile profile : profiles){
            Long accountId = profile.getAccount().getAccountId();
            if(accountCounts.containsKey(accountId)){
                int count = accountCounts.get(accountId);
                accountCounts.put(accountId, count+1);
            }else{
                accountCounts.put(accountId, 1);
            }
        }
        List<Profile> singleWarningCount = new ArrayList<>();
        Set<Long> doubleWarningCountList = new HashSet<>();
        for(Profile profile : profiles){
            Long accountId = profile.getAccount().getAccountId();
            int count = accountCounts.get(accountId);
            if(count == 1){
                singleWarningCount.add(profile);
            } else if (count == 2) {
                doubleWarningCountList.add(accountId);
            }
        }
        List<Profile> doubleWarningCount = doubleWarningCountList.stream()
                .map(accountId -> profileRepository.findByAccount_AccountId(accountId)
                        .orElseThrow(() -> new IllegalArgumentException("profile 없음")))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(requestForm.getWarningCounts() == 1){
            return makeAccountListForm(singleWarningCount, pageable);
        }
        if (requestForm.getWarningCounts() == 2) {
            return makeAccountListForm(doubleWarningCount, pageable);
        }
        if (requestForm.getWarningCounts()  == 3) {
            return makeBlacklistForm(pageable);
        }
        return null;
    }

    private Page<AdminAccountListForm> makeAccountListForm(List<Profile> WarningCount, Pageable pageable) {
        final List<AdminAccountListForm> adminAccountListForms = new ArrayList<>();
        for (Profile isProfile : WarningCount) {
            Account isAccount = accountRepository.findById(isProfile.getAccount().getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("account 없음"));
            List<Playlist> playlists = playlistRepository.findPlaylistIdByAccount(isAccount);
            Integer playlistCounts = playlists.size();
            Integer songCounts = 0;
            for (Playlist playlist : playlists) {
                songCounts += songRepository.countByPlaylist(playlist);
            }
            Integer warningCounts = warningRepository.countByAccount(isAccount);
            AdminAccountListForm adminAccountListForm = new AdminAccountListForm(isProfile.getAccount().getAccountId(), isProfile.getNickname(), playlistCounts, songCounts, isProfile.getAccount().getCreateDate(), warningCounts);
            adminAccountListForms.add(adminAccountListForm);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), adminAccountListForms.size());
        return new PageImpl<>(adminAccountListForms.subList(start, end), pageable, adminAccountListForms.size());
    }
    public Page<AdminAccountListForm> makeBlacklistForm(Pageable pageable) {
        AccountRoleType roleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
        List<Profile> profileList = profileRepository.findAllBlacklistWithPage(pageable, roleType);
        final List<AdminAccountListForm> adminAccountListForms = new ArrayList<>();
        for (Profile isProfile : profileList) {
            Account isAccount = accountRepository.findAccountByAccountRoleType(roleType)
                    .orElseThrow(() -> new IllegalArgumentException("account 없음"));

            List<Playlist> playlists = playlistRepository.findPlaylistIdByAccount(isAccount);
            Integer playlistCounts = playlists.size();
            Integer songCounts = 0;
            for (Playlist playlist : playlists) {
                songCounts += songRepository.countByPlaylist(playlist);
            }
            Integer warningCounts = warningRepository.countByAccount(isAccount);
            AdminAccountListForm adminAccountListForm = new AdminAccountListForm(isProfile.getAccount().getAccountId(), isProfile.getNickname(), playlistCounts, songCounts, isProfile.getAccount().getCreateDate(), warningCounts);
            adminAccountListForms.add(adminAccountListForm);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), adminAccountListForms.size());

        return new PageImpl<>(
                adminAccountListForms.subList(start, end),
                pageable,
                adminAccountListForms.size()
        );
    }

    @Override
    public void changeBadNickname(HttpHeaders headers, Long accountId) {
        if (checkAdmin(headers)) return;

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account 없음"));
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("profile 없음"));

        String newNickname = makeNewNickname();
        boolean duplicateNickname = true;

        while(duplicateNickname){
            Optional<Profile> maybeProfile = profileRepository.findByNickname(newNickname);
            if(maybeProfile.isPresent()){
                newNickname = makeNewNickname();
            } else {
                duplicateNickname = false;
            }
        }

        profile.setNickname(newNickname);
        profileRepository.save(profile);
    }

    private String makeNewNickname(){
        String[] genreList = {"락", "발라드", "힙합", "클래식", "재즈", "레게", "트로트", "알앤비"};

        RandomValue randomValue = new RandomValue();
        int value = randomValue.randomValue(genreList.length);

        String randomAlphabet = "";
        String randomNumber = "";

        for(int i=0; i<2; i++){
            randomAlphabet += (String.valueOf((char) ((Math.random() * 26) + 65)));
            randomNumber += String.valueOf(randomValue.randomValue(9));
        }

        return genreList[value] + "Muser" + randomAlphabet + randomNumber;
    }

    @Override
    public InquiryCountResponseForm countInquiry(HttpHeaders headers){
        if (checkAdmin(headers)) return null;

        Long date = System.currentTimeMillis();

        SimpleDateFormat sdt = new SimpleDateFormat();
        sdt.applyPattern("yyyy-MM-dd");
        String currentDate = sdt.format(date);

        LocalDate transformCurrentDate = TransformToDate.transformToDate(currentDate);
        int todayInquiryCount = inquiryRepository.countByCreateInquiryDate(transformCurrentDate);
        int waitingAnswerInquiryCount = inquiryRepository.countWaitingAnswer();
        long totalInquiryCount = inquiryRepository.count();

        return new InquiryCountResponseForm(todayInquiryCount, waitingAnswerInquiryCount, totalInquiryCount);
    }
}

