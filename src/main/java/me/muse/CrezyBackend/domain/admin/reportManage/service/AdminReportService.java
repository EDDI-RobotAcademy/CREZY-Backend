package me.muse.CrezyBackend.domain.admin.reportManage.service;

import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.*;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface AdminReportService {
    Integer getTotalPage();
    List<ReportResponseForm> list(Integer page, HttpHeaders headers);
    boolean processingReport(ReportProcessingForm processingForm, HttpHeaders headers);
    ReportReadResponseForm readReport(Long reportId, HttpHeaders headers);
    ReportReadAccountResponseForm readAccountReport(Long reportId, HttpHeaders headers);
    ReportReadPlaylistResponseForm readPlaylistReport(Long reportId, HttpHeaders headers);
    ReportReadSongResponseForm readSongReport(Long reportId, HttpHeaders headers);
    void deleteWarning(Long warningId, HttpHeaders headers);
}
