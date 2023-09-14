package me.muse.CrezyBackend.domain.report.service;

import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import org.springframework.http.HttpHeaders;

public interface ReportService {
    long registerReport(ReportRegisterForm reportRegisterForm, HttpHeaders headers);
}
