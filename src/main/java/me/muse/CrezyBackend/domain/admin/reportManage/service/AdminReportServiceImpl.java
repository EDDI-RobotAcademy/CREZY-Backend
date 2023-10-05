package me.muse.CrezyBackend.domain.admin.reportManage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryDetailRepository;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.accountManage.service.AdminAccountService;
import me.muse.CrezyBackend.domain.admin.playlistManage.service.AdminPlaylistService;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.*;
import me.muse.CrezyBackend.domain.admin.songManage.service.AdminSongService;
import me.muse.CrezyBackend.domain.likePlaylist.entity.LikePlaylist;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.entity.*;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportedCategoryTypeRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import me.muse.CrezyBackend.domain.warning.repository.WarningRepository;
import me.muse.CrezyBackend.utility.checkAdmin.CheckAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.BLACKLIST;
import static me.muse.CrezyBackend.domain.report.entity.ReportStatus.*;
import static me.muse.CrezyBackend.domain.report.entity.ReportedCategory.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {
    final private ReportRepository reportRepository;
    final private ReportDetailRepository reportDetailRepository;
    final private AccountRepository accountRepository;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private WarningRepository warningRepository;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    final private ProfileRepository profileRepository;
    final private PlaylistRepository playlistRepository;
    final private SongRepository songRepository;
    final private CheckAdmin checkAdmin;
    final private InquiryDetailRepository inquiryDetailRepository;
    final private LikePlaylistRepository likePlaylistRepository;
    final private AdminSongService adminSongService;
    final private AdminPlaylistService adminPlaylistService;
    final private AdminAccountService adminAccountService;
    final private RedisService redisService;
    final private ReportedCategoryTypeRepository reportedCategoryTypeRepository;

    @Override
    public Page<ReportResponseForm> list(ReportListRequestForm requestForm, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10);


        Integer SongReportCount = reportRepository.countByReportedCategoryType(SONG);
        Integer PlaylistReportCount = reportRepository.countByReportedCategoryType(PLAYLIST);
        Integer AccountReportCount = reportRepository.countByReportedCategoryType(ACCOUNT);

        List<ReportDetail> reportDetailList = new ArrayList<>();
        ReportStatus statusType;
        ReportedCategory categoryType;
        if(requestForm.getCategoryType().equals("TOTAL")){
            switch (requestForm.getStatusType()){
                case "TOTAL" -> reportDetailList = reportDetailRepository.findAllWithPage();
                case "APPROVED", "RETURNED", "HOLDON" -> {
                        statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.valueOf(requestForm.getStatusType())).get().getReportStatus();
                        reportDetailList = reportDetailRepository.findByReportStatusType(statusType);
                }
            }
        } else{
            categoryType = reportedCategoryTypeRepository.findByReportedCategory(ReportedCategory.valueOf(requestForm.getCategoryType())).get().getReportedCategory();
            switch (requestForm.getStatusType()){
                case "TOTAL" -> reportDetailList = reportDetailRepository.findByReportedCategoryType(categoryType);
                case "APPROVED", "RETURNED", "HOLDON" ->{
                    statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.valueOf(requestForm.getStatusType())).get().getReportStatus();
                    reportDetailList = reportDetailRepository.findByReportStatusTypeAndReportedCategoryType(statusType, categoryType);
                }
            }
        }

        List<ReportResponseForm> reportResponseForms = new ArrayList<>();
        String reporterNickname;

        for (ReportDetail reportDetail : reportDetailList) {
         Optional<Profile> reporterAccount = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId());
         if(reporterAccount.isEmpty()){
             reporterNickname = "탈퇴한 회원";
         }else{
             reporterNickname = reporterAccount.get().getNickname();
         }

            ReportResponseForm responseForm = new ReportResponseForm(
                    reportDetail.getReport().getReportId(),
                    reporterNickname,
                    reportDetail.getReportedId(),
                    reportDetail.getReportContent(),
                    reportDetail.getReport().getReportedCategoryType().getReportedCategory().toString(),
                    reportDetail.getReport().getReportStatusType().getReportStatus().toString(),
                    reportDetail.getCreateReportDate(),
                    SongReportCount,
                    PlaylistReportCount,
                    AccountReportCount);

            reportResponseForms.add(responseForm);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reportResponseForms.size());

        return new PageImpl<>(
                reportResponseForms.subList(start, end),
                pageable,
                reportResponseForms.size()
        );
    }

    @Override
    public Integer getTotalPage() {
        Integer totalReport = (int) reportRepository.count();
        Integer size = 10;
        if (totalReport % size == 0) {
            return totalReport / size;
        } else {
            return totalReport / size + 1;
        }
    }

    @Override
    public boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers) throws GeneralSecurityException, IOException {
        if (!checkAdmin.checkAdmin(headers)) return false;

        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.valueOf(processingForm.getReportStatus())).get();

        Report report = reportRepository.findById(processingForm.getReportId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        ReportDetail reportDetail = reportDetailRepository.findById(report.getReportId())
                .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
        Account reportedAccount = accountRepository.findById(reportDetail.getReportedAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReportedAccount not found"));

        report.setReportStatusType(statusType);

        if (processingForm.getReportStatus().equals("APPROVE")) {
            if(report.getReportedCategoryType().getReportedCategory() == SONG){

                Song song = songRepository.findById(reportDetail.getReportedId())
                        .orElseThrow(() -> new IllegalArgumentException("Song not found"));

                switch (reportDetail.getReportContent()) {
                    case "저작권 침해", "잘못된 링크", "허위" -> {
                        song.setLink(" ");
                        songRepository.save(song);
                        adminSongService.registerSongStatusBlock(song.getSongId(), headers);
                    }
                    case "불쾌한 콘텐츠" -> {
                        songRepository.deleteById(song.getSongId());
                        warningRepository.save(new Warning(reportedAccount, report));
                    }
                    case "노래 가사 오류" -> {
                        song.setLyrics("가사 수정이 필요하여 내용을 삭제했습니다.");
                        songRepository.save(song);
                    }
                    default -> adminSongService.registerSongStatusBlock(song.getSongId(), headers);
                }
            } else if (report.getReportedCategoryType().getReportedCategory() == PLAYLIST) {
                try{
                    Playlist playlist = playlistRepository.findById(reportDetail.getReportedId())
                            .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

                    switch (reportDetail.getReportContent()) {
                        case "부적절한 제목" -> adminPlaylistService.changePlaylistName(headers, playlist.getPlaylistId());
                        case "유해한 플레이리스트 사진" -> {
                            playlist.setThumbnailName(null);
                            playlistRepository.save(playlist);
                        }
                    }
                    warningRepository.save(new Warning(reportedAccount, report));
                }catch (IllegalArgumentException e){
                    return false;
                }
            } else if (report.getReportedCategoryType().getReportedCategory() == ACCOUNT) {
                try {
                    Profile profile = profileRepository.findByAccount(reportedAccount)
                            .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

                    switch (reportDetail.getReportContent()) {
                        case "부적절한 닉네임", "불법 광고 계정", "계정 도용" -> adminAccountService.changeBadNickname(headers, reportedAccount.getAccountId());
                        case "유해한 프로필 사진" -> {
                            profile.setProfileImageName(null);
                            profileRepository.save(profile);
                        }
                    }
                    warningRepository.save(new Warning(reportedAccount, report));
                }catch (IllegalArgumentException e){
                    return false;
                }
            }
            if (warningRepository.countByAccount(reportedAccount) >= 3) {
                AccountRoleType accountRoleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
                reportedAccount.setRoleType(accountRoleType);
                accountRepository.save(reportedAccount);
            }
        }else {
            Optional<Warning> maybeWarning = warningRepository.findByReport_ReportId(report.getReportId());
            maybeWarning.ifPresent(warning -> warningRepository.deleteById(warning.getWarningId()));
        }

        reportRepository.save(report);
        return true;
    }

    @Override
    public ReportReadResponseForm readReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
        Optional<Profile> maybeReporterProfile = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId());
        String reporterNickname;

        if(maybeReporterProfile.isEmpty()){
            reporterNickname = "탈퇴한 회원";
        }else{
            reporterNickname = maybeReporterProfile.get().getNickname();
        }
        Account reportedAccount = accountRepository.findById(reportDetail.getReportedAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReportedAccount not found"));
        Profile reportedProfile = profileRepository.findByAccount(reportedAccount)
                .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));

        Report report = reportDetail.getReport();
        ReportReadResponseForm responseForm = new ReportReadResponseForm(
                report, reportDetail.getReportContent(), reporterNickname, reportedProfile.getNickname(),
                reportDetail.getCreateReportDate());
        return responseForm;
    }

    @Override
    @Transactional
    public ReportReadAccountResponseForm readAccountReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        try {
            ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                    .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
            Profile reportedProfile = profileRepository.findByAccount_AccountId(reportDetail.getReportedAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));
            Optional<Profile> maybeReporterProfile = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId());
            String reporterNickname;

            if(maybeReporterProfile.isEmpty()){
                reporterNickname = "탈퇴한 회원";
            }else{
                reporterNickname = maybeReporterProfile.get().getNickname();
            }

            int reportedCounts = reportDetailRepository.findAllByReportedAccountId(reportDetail.getReportedAccountId()).size();
            int warningCounts = warningRepository.countByAccount(reportedProfile.getAccount());
            int inquiryCounts = inquiryDetailRepository.findByProfile_Account_accountId(reportDetail.getReportedAccountId()).size();

            ReportReadAccountResponseForm responseForm = new ReportReadAccountResponseForm(
                    reporterNickname,
                    reportedProfile.getNickname(),
                    reportedProfile.getProfileImageName(),
                    reportDetail.getReport().getReportedCategoryType().getReportedCategory().toString(),
                    reportedCounts,
                    warningCounts,
                    inquiryCounts,
                    reportedProfile.getAccount().getAccountId());
            return responseForm;
        } catch (IllegalArgumentException e){
            return new ReportReadAccountResponseForm("탈퇴한 회원입니다.");
        }
    }

    @Override
    @Transactional
    public ReportReadPlaylistResponseForm readPlaylistReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;
        try {
            ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                    .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
            Profile reportedProfile = profileRepository.findByAccount_AccountId(reportDetail.getReportedAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));
            Optional<Profile> maybeReporterProfile = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId());
            String reporterNickname;

            if(maybeReporterProfile.isEmpty()){
                reporterNickname = "탈퇴한 회원";
            }else{
                reporterNickname = maybeReporterProfile.get().getNickname();
            }

            Playlist playlist = playlistRepository.findById(reportDetail.getReportedId())
                    .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

            List<Song> songlist = songRepository.findByPlaylist_PlaylistId(playlist.getPlaylistId());
            List<LikePlaylist> likePlaylists = likePlaylistRepository.findByPlaylist(playlist);

            ReportReadPlaylistResponseForm responseForm = new ReportReadPlaylistResponseForm(
                    reporterNickname,
                    reportedProfile.getNickname(),
                    playlist.getPlaylistName(),
                    playlist.getThumbnailName(),
                    reportDetail.getReport().getReportedCategoryType().getReportedCategory().toString(),
                    songlist.size(),
                    likePlaylists.size(),
                    reportDetail.getReportedId());
            return responseForm;
        } catch (IllegalArgumentException e){
            return new ReportReadPlaylistResponseForm("삭제된 플레이리스트입니다.");
        }
    }

    @Override
    @Transactional
    public ReportReadSongResponseForm readSongReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        try{
            ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                    .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
            Profile reportedProfile = profileRepository.findByAccount_AccountId(reportDetail.getReportedAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));
            Optional<Profile> maybeReporterProfile = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId());
            String reporterNickname;

            if(maybeReporterProfile.isEmpty()){
                reporterNickname = "탈퇴한 회원";
            }else{
                reporterNickname = maybeReporterProfile.get().getNickname();
            }

            Song song = songRepository.findById(reportDetail.getReportedId())
                    .orElseThrow(() -> new IllegalArgumentException("Song not found"));

            ReportReadSongResponseForm responseForm = new ReportReadSongResponseForm(
                    reporterNickname,
                    reportedProfile.getNickname(),
                    song.getPlaylist().getPlaylistName(),
                    song.getTitle(),
                    song.getSinger(),
                    song.getLink(),
                    song.getLyrics(),
                    reportDetail.getReport().getReportedCategoryType().getReportedCategory().toString(),
                    song.getSongId());
            return responseForm;
        }catch (IllegalArgumentException e){
            return new ReportReadSongResponseForm("삭제된 노래입니다.");
        }
    }

    @Override
    public ReportCountResponseForm countReport(HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        int approveCount = reportRepository.countByReportStatusType_ReportStatus(APPROVE);
        int returnCount = reportRepository.countByReportStatusType_ReportStatus(RETURN);
        int holdonCount = reportRepository.countByReportStatusType_ReportStatus(HOLDON);
        int totalCount = (int)reportRepository.count();

        return new ReportCountResponseForm(approveCount, returnCount, holdonCount, totalCount);
    }
    @Override
    @Transactional
    public long registerReport(ReportRegisterForm reportRegisterForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return -1;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Account reportedAccount = new Account();
        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.HOLDON).get();
        ReportedCategoryType categoryType = reportedCategoryTypeRepository.findByReportedCategory(ReportedCategory.valueOf(reportRegisterForm.getReportedCategoryType())).get();
        if(categoryType.getReportedCategory() == SONG){
            Long playlistId = songRepository.findById(reportRegisterForm.getReportedId()).get().getPlaylist().getPlaylistId();
            reportedAccount = accountRepository.findByPlaylist_playlistId(playlistId)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        }
        if(categoryType.getReportedCategory() == PLAYLIST){
            reportedAccount = accountRepository.findByPlaylist_playlistId(reportRegisterForm.getReportedId())
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        }
        if(categoryType.getReportedCategory() == ACCOUNT){
            reportedAccount = accountRepository.findById(reportRegisterForm.getReportedId())
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        }
        final Report report = new Report(categoryType, statusType);
        final ReportDetail reportDetail = new ReportDetail(account.getAccountId(), reportedAccount.getAccountId(), reportRegisterForm.getReportedId(), reportRegisterForm.getReportContent(), report);

        return reportDetailRepository.save(reportDetail).getReportDetailId();
    }
}
