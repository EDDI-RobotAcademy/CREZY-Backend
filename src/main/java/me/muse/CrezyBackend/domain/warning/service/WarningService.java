package me.muse.CrezyBackend.domain.warning.service;

import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import org.springframework.http.HttpHeaders;

public interface WarningService {
    Warning registWarning(HttpHeaders headers, ReportRegisterForm requestForm);
    void deleteWarning(Long warningId, HttpHeaders headers);
}
