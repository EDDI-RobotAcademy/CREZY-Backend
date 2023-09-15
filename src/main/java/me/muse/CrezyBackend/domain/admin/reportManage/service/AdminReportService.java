package me.muse.CrezyBackend.domain.admin.reportManage.service;

import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportResponseForm;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportReadResponseForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface AdminReportService {
    Integer getTotalPage();
    List<ReportResponseForm> list(Integer page, HttpHeaders headers);
    boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers);
    ReportReadResponseForm readReport(Long reportId, HttpHeaders headers);

}
