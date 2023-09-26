package me.muse.CrezyBackend.domain.report.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.entity.*;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportedCategoryTypeRepository;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.report.entity.ReportedCategory.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    final private ReportDetailRepository reportDetailRepository;
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private ReportedCategoryTypeRepository reportedCategoryTypeRepository;
    final private SongRepository songRepository;

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
