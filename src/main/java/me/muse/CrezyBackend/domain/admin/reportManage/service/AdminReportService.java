package me.muse.CrezyBackend.domain.admin.reportManage.service;

import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.*;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface AdminReportService {
    Integer getTotalPage();
    List<ReportResponseForm> list(Integer page, HttpHeaders headers);
    boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers) throws GeneralSecurityException, IOException;
    ReportReadResponseForm readReport(Long reportId, HttpHeaders headers);
    ReportReadAccountResponseForm readAccountReport(Long reportId, HttpHeaders headers);
    ReportReadPlaylistResponseForm readPlaylistReport(Long reportId, HttpHeaders headers);
    ReportReadSongResponseForm readSongReport(Long reportId, HttpHeaders headers);

    ReportCountResponseForm countReport(HttpHeaders headers);
}
