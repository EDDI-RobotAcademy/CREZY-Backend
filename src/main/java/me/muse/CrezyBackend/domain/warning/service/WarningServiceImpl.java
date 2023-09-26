package me.muse.CrezyBackend.domain.warning.service;

import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.admin.reportManage.service.AdminReportService;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import me.muse.CrezyBackend.domain.report.entity.ReportStatus;
import me.muse.CrezyBackend.domain.report.entity.ReportStatusType;
import me.muse.CrezyBackend.domain.report.repository.ReportDetailRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.report.service.ReportService;
import me.muse.CrezyBackend.domain.warning.controller.form.WarningResponseForm;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import me.muse.CrezyBackend.domain.warning.repository.WarningRepository;
import me.muse.CrezyBackend.utility.checkAdmin.CheckAdmin;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarningServiceImpl implements WarningService{
    final private ReportDetailRepository reportDetailRepository;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private WarningRepository warningRepository;
    final private CheckAdmin checkAdmin;
    final private AdminReportService adminReportService;
    final private ReportService reportService;
    final private ReportRepository reportRepository;

    @Override
    @Transactional
    public void registWarning(HttpHeaders headers, ReportRegisterForm requestForm) {
        if(!checkAdmin.checkAdmin(headers)) return;

        Long reportDetailId = reportService.registerReport(requestForm, headers);

        final ReportDetail reportDetail = reportDetailRepository.findById(reportDetailId)
                .orElseThrow(() -> new IllegalArgumentException("ReportDetail not found"));

        ReportProcessingForm processingForm = new ReportProcessingForm(reportDetail.getReport().getReportId(), "APPROVE");

        adminReportService.processingReport(processingForm, headers);
    }

    @Override
    public void deleteWarning(Long warningId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return;
        Warning warning = warningRepository.findById(warningId)
                .orElseThrow(()->new IllegalArgumentException("report 없음"));

        Report report = warning.getReport();
        ReportStatusType statusType = reportStatusTypeRepository.findByReportStatus(ReportStatus.RETURN).get();
        report.setReportStatusType(statusType);

        reportRepository.save(report);
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
