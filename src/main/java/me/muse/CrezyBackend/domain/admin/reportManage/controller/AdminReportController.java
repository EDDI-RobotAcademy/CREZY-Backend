package me.muse.CrezyBackend.domain.admin.reportManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.*;
import me.muse.CrezyBackend.domain.admin.reportManage.service.AdminReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-report")
public class AdminReportController {
    final private AdminReportService adminService;
    @GetMapping(value = "/list")
    public List<ReportResponseForm> reportList(@RequestParam("page") Integer page, @RequestHeader HttpHeaders headers) {
        log.info("reportList()");
        return adminService.list(page, headers);
    }
    @GetMapping("/list/total-page")
    public Integer getTotalPage() {
        return adminService.getTotalPage();
    }
    @PostMapping("/processing")
    public boolean approveReport(@RequestBody ReportProcessingForm processingForm, @RequestHeader HttpHeaders headers) {
        log.info("approveReport()");
        return adminService.processingReport(processingForm, headers);
    }
    @GetMapping("/read-report")
    public ReportReadResponseForm readReport(@RequestParam("reportId") Long reportId, @RequestHeader HttpHeaders headers) {
        log.info("readReport()");
        return adminService.readReport(reportId,headers);
    }
    @GetMapping("/read-account-report")
    public ReportReadAccountResponseForm readAccountReport(@RequestParam("reportId") Long reportId, @RequestHeader HttpHeaders headers) {
        log.info("readAccountReport()");
        return adminService.readAccountReport(reportId,headers);
    }
    @GetMapping("/read-playlist-report")
    public ReportReadPlaylistResponseForm readPlaylistReport(@RequestParam("reportId") Long reportId, @RequestHeader HttpHeaders headers) {
        log.info("readPlaylistReport()");
        return adminService.readPlaylistReport(reportId,headers);
    }
    @GetMapping("/read-song-report")
    public ReportReadSongResponseForm readSongReport(@RequestParam("reportId") Long reportId, @RequestHeader HttpHeaders headers) {
        log.info("readSongReport()");
        return adminService.readSongReport(reportId,headers);
    }
}
