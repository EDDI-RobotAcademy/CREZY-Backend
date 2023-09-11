package me.muse.CrezyBackend.domain.report.service;

import me.muse.CrezyBackend.domain.report.controller.form.ReportProcessingForm;
import org.springframework.http.HttpHeaders;

public interface ReportService {
    boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers);
}
