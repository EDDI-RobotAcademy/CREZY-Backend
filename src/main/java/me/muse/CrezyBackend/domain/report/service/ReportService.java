package me.muse.CrezyBackend.domain.report.service;

import me.muse.CrezyBackend.domain.report.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.report.controller.form.ReportReadResponseForm;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.controller.form.ReportResponseForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface ReportService {
    List<ReportResponseForm> list(Integer page, HttpHeaders headers);
    Integer getTotalPage();
    boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers);
    ReportReadResponseForm readReport(Long reportId, HttpHeaders headers);
}
