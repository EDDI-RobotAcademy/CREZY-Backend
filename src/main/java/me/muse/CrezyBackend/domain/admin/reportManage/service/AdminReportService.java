package me.muse.CrezyBackend.domain.admin.reportManage.service;

import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.*;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface AdminReportService {
    Integer getTotalPage();
    Page<ReportResponseForm> list(ReportListRequestForm requestForm, HttpHeaders headers);
    boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers) throws GeneralSecurityException, IOException;
    ReportReadResponseForm readReport(Long reportId, HttpHeaders headers);
    ReportReadAccountResponseForm readAccountReport(Long reportId, HttpHeaders headers);
    ReportReadPlaylistResponseForm readPlaylistReport(Long reportId, HttpHeaders headers);
    ReportReadSongResponseForm readSongReport(Long reportId, HttpHeaders headers);

    ReportCountResponseForm countReport(HttpHeaders headers);
    long registerReport(ReportRegisterForm reportRegisterForm, HttpHeaders headers);
}
