package me.muse.CrezyBackend.domain.admin.reportManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.*;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import me.muse.CrezyBackend.domain.report.entity.ReportStatusType;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import me.muse.CrezyBackend.domain.warning.repository.WarningRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.BLACKLIST;
import static me.muse.CrezyBackend.domain.report.entity.ReportStatus.APPROVE;
import static me.muse.CrezyBackend.domain.report.entity.ReportedCategory.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {
    final private ReportRepository reportRepository;
    final private ReportDetailRepository reportDetailRepository;
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private WarningRepository warningRepository;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    final private ProfileRepository profileRepository;
    final private PlaylistRepository playlistRepository;

    @Override
    public List<ReportResponseForm> list(Integer page, HttpHeaders headers) {
//        if (!checkAdmin(headers)) return null;

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("reportDetailId").descending());
        List<ReportDetail> reportDetailList = reportDetailRepository.findAllWithPage(pageable);

        Integer SongReportCount = reportRepository.countByReportedCategoryType(SONG);
        Integer PlaylistReportCount = reportRepository.countByReportedCategoryType(PLAYLIST);
        Integer AccountReportCount = reportRepository.countByReportedCategoryType(ACCOUNT);

        List<ReportResponseForm> reportResponseForms = new ArrayList<>();
        for (ReportDetail reportDetail : reportDetailList) {
            ReportResponseForm responseForm = new ReportResponseForm(
                    reportDetail.getReport().getReportId(), reportDetail.getReportedId(), reportDetail.getReportContent(),
                    reportDetail.getReport().getReportedCategoryType().getReportedCategory().toString(), reportDetail.getReport().getReportStatusType().getReportStatus().toString(),
                    reportDetail.getCreateReportDate(), SongReportCount, PlaylistReportCount, AccountReportCount);

            reportResponseForms.add(responseForm);
        }
        return reportResponseForms;
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
    public boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers) {
        if (!checkAdmin(headers))
            return false;

        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(processingForm.getReportStatus()).get();

        Report report = reportRepository.findById(processingForm.getReportId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        report.setReportStatusType(statusType);

        if (processingForm.getReportStatus() == APPROVE) {
            ReportDetail reportDetail = reportDetailRepository.findById(report.getReportId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
            Account reportedAccount = accountRepository.findById(reportDetail.getReportedAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportedAccount not found"));

            warningRepository.save(new Warning(reportedAccount, report));

            if (warningRepository.countByAccount(reportedAccount) >= 3) {
                AccountRoleType accountRoleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
                reportedAccount.setRoleType(accountRoleType);
                accountRepository.save(reportedAccount);
            }
        }

        reportRepository.save(report);
        return true;
    }
    @Override
    public ReportReadResponseForm readReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin(headers)) return null;

        ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
        Account reporterAccount = accountRepository.findById(reportDetail.getReporterAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReporterAccount not found"));
        Profile reporterProfile = profileRepository.findByAccount(reporterAccount)
                .orElseThrow(() -> new IllegalArgumentException("ReporterProfile not found"));
        Account reportedAccount = accountRepository.findById(reportDetail.getReportedAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReportedAccount not found"));
        Profile reportedProfile = profileRepository.findByAccount(reportedAccount)
                .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));

        Report report = reportDetail.getReport();
        ReportReadResponseForm responseForm = new ReportReadResponseForm(
                report, reportDetail.getReportContent(), reporterProfile.getNickname(), reportedProfile.getNickname(),
                reportDetail.getCreateReportDate());
        return responseForm;
    }
    private boolean checkAdmin(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getRoleType().getRoleType() != ADMIN) {
            return false;
        }
        return true;
    }

    @Override
    public ReportReadAccountResponseForm readAccountReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin(headers)) return null;

        ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
        Profile reportedProfile = profileRepository.findByAccount_AccountId(reportDetail.getReportedAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));
        Profile reporterProfile = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReporterProfile not found"));

        ReportReadAccountResponseForm responseForm = new ReportReadAccountResponseForm(
                reporterProfile.getNickname(),
                reportedProfile.getNickname(),
                reportedProfile.getProfileImageName());
        return responseForm;
    }

    @Override
    public ReportReadPlaylistResponseForm readPlaylistReport(Long reportId, HttpHeaders headers) {
        if (!checkAdmin(headers)) return null;

        ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(reportId)
                .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
        Playlist playlist = playlistRepository.findById(reportDetail.getReportedId())
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));
        Profile reportedProfile = profileRepository.findByAccount_AccountId(reportDetail.getReportedAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReportedProfile not found"));
        Profile reporterProfile = profileRepository.findByAccount_AccountId(reportDetail.getReporterAccountId())
                .orElseThrow(() -> new IllegalArgumentException("ReporterProfile not found"));

        ReportReadPlaylistResponseForm responseForm = new ReportReadPlaylistResponseForm(
                reporterProfile.getNickname(),
                reportedProfile.getNickname(),
                playlist.getPlaylistName(),
                playlist.getThumbnailName());
        return responseForm;
    }
}

