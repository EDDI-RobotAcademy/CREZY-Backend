package me.muse.CrezyBackend.domain.report.service;

import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.report.controller.form.ReportResponseForm;
import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.report.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import me.muse.CrezyBackend.domain.report.entity.ReportStatusType;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import me.muse.CrezyBackend.domain.warning.repository.WarningRepository;
import java.util.List;
import java.util.Objects;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.BLACKLIST;
import static me.muse.CrezyBackend.domain.report.entity.ReportStatus.APPROVE;
import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;
import static me.muse.CrezyBackend.domain.report.entity.ReportedCategory.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
    final private ReportRepository reportRepository;
    final private ReportDetailRepository reportDetailRepository;
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    @Override
    public List<ReportResponseForm> list(Integer page, HttpHeaders headers) {

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new IllegalArgumentException("Account not found"));

        if(account.getRoleType().getRoleType() != ADMIN){
            return null;
        }

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("reportId").descending());
        List<ReportDetail> reportDetailList = reportDetailRepository.findAllwithPage(pageable);

        Integer SongReportCount = reportRepository.findByReportedCategoryType(SONG).size();
        Integer PlaylistReportCount = reportRepository.findByReportedCategoryType(PLAYLIST).size();
        Integer AccountReportCount = reportRepository.findByReportedCategoryType(ACCOUNT).size();

        List<ReportResponseForm> reportResponseForms = new ArrayList<>();
        for(ReportDetail reportDetail : reportDetailList) {
            ReportResponseForm responseForm = new ReportResponseForm(
                    reportDetail.getReport().getReportId(), reportDetail.getReportContent(),
                    reportDetail.getReport().getReportedCategoryType().toString(), reportDetail.getReport().getReportStatusType().toString(),
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private ReportRepository reportRepository;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private WarningRepository warningRepository;
    final private ReportDetailRepository reportDetailRepository;
    final private AccountRoleTypeRepository accountRoleTypeRepository;

    @Override
    public boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if(account.getRoleType().getRoleType() != ADMIN){
            return false;
        }

        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(processingForm.getReportStatus()).get();

        Report report = reportRepository.findById(processingForm.getReportId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        report.setReportStatusType(statusType);

        if(processingForm.getReportStatus() == APPROVE){
            ReportDetail reportDetail = reportDetailRepository.findById(report.getReportId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));
            Account reportedAccount = accountRepository.findById(reportDetail.getReportedAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("ReportedAccount not found"));

            warningRepository.save(new Warning(reportedAccount, report));

            if(warningRepository.countByAccount(reportedAccount) >= 3){
                AccountRoleType accountRoleType = accountRoleTypeRepository.findByRoleType(BLACKLIST).get();
                reportedAccount.setRoleType(accountRoleType);
                accountRepository.save(reportedAccount);
            }
        }

        reportRepository.save(report);
        return true;
    }
}
