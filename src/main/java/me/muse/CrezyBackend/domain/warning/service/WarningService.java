package me.muse.CrezyBackend.domain.warning.service;

import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.warning.controller.form.WarningResponseForm;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface WarningService {
    void registWarning(HttpHeaders headers, ReportRegisterForm requestForm) throws GeneralSecurityException, IOException;
    void deleteWarning(Long warningId, HttpHeaders headers);

    List<WarningResponseForm> searchByAccount(HttpHeaders headers, Long accountId);
}
