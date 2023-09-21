package me.muse.CrezyBackend.domain.warning.service;

import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.warning.controller.form.WarningResponseForm;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface WarningService {
    Warning registWarning(HttpHeaders headers, ReportRegisterForm requestForm);
    void deleteWarning(Long warningId, HttpHeaders headers);

    List<WarningResponseForm> searchByAccount(HttpHeaders headers, Long accountId);
}
