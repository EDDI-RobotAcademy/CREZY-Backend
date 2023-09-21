package me.muse.CrezyBackend.domain.warning.service;

import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.entity.*;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportedCategoryTypeRepository;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.domain.warning.controller.form.WarningResponseForm;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import me.muse.CrezyBackend.domain.warning.repository.WarningRepository;
import me.muse.CrezyBackend.utility.checkAdmin.CheckAdmin;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.BLACKLIST;
import static me.muse.CrezyBackend.domain.report.entity.ReportedCategory.SONG;

@Service
@RequiredArgsConstructor
public class WarningServiceImpl implements WarningService{
    final private ReportDetailRepository reportDetailRepository;
    final private ReportRepository reportRepository;
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private ReportedCategoryTypeRepository reportedCategoryTypeRepository;
    final private SongRepository songRepository;
    final private WarningRepository warningRepository;
    final private AccountRoleTypeRepository accountRoleTypeRepository;
    final private CheckAdmin checkAdmin;

    @Override
    @Transactional
    public void registWarning(HttpHeaders headers, ReportRegisterForm requestForm) {
        if(checkAdmin.checkAdmin(headers)) return;

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account reporterAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Account reportedAccount;

        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.APPROVE).get();
        ReportedCategoryType categoryType = reportedCategoryTypeRepository.findByReportedCategory(ReportedCategory.valueOf(requestForm.getReportedCategoryType())).get();

        if(categoryType.getReportedCategory() == SONG){
            Long playlistId = songRepository.findById(requestForm.getReportedId()).get().getPlaylist().getPlaylistId();
            reportedAccount = accountRepository.findByPlaylist_playlistId(playlistId)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        }else{
            reportedAccount = accountRepository.findByPlaylist_playlistId(requestForm.getReportedId())
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        }

        final Report report = new Report(categoryType, statusType);
        final ReportDetail reportDetail = new ReportDetail(reporterAccount.getAccountId(), reportedAccount.getAccountId(), requestForm.getReportedId(), requestForm.getReportContent(), report);
        final Warning warning = new Warning(reportedAccount, report);

        reportRepository.save(report);
        reportDetailRepository.save(reportDetail);
        warningRepository.save(warning);

        if (warningRepository.countByAccount(reportedAccount) >= 3) {
            AccountRoleType accountRoleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
            reportedAccount.setRoleType(accountRoleType);
            accountRepository.save(reportedAccount);
        }
    }

    @Override
    public void deleteWarning(Long warningId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return;
        Warning warning = warningRepository.findById(warningId)
                .orElseThrow(()->new IllegalArgumentException("report 없음"));

        Report report = warning.getReport();
        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.RETURN).get();
        report.setReportStatusType(statusType);

        warningRepository.deleteById(warningId);
    }

    @Override
    public List<WarningResponseForm> searchByAccount(HttpHeaders headers, Long accountId) {
        if (!checkAdmin.checkAdmin(headers)) return null;
        List<WarningResponseForm> responseFormList = new ArrayList<>();

        List<Warning> warningList = warningRepository.findByAccount_AccountId(accountId);

        for(Warning warning : warningList){
            ReportDetail reportDetail = reportDetailRepository.findByReport_ReportId(warning.getReport().getReportId())
                    .orElseThrow(()->new IllegalArgumentException("reportDetail 없음"));

            responseFormList.add(
                    new WarningResponseForm(
                            warning.getWarningId(),
                            warning.getCreateWarningDate(),
                            warning.getReport().getReportedCategoryType().getReportedCategory().toString(),
                            reportDetail.getReportContent(),
                            reportDetail.getCreateReportDate()));
        }

        return responseFormList;
    }
}
