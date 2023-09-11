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
import java.util.List;
import java.util.Objects;

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


}
