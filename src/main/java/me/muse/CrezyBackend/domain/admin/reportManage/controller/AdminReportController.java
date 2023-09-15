package me.muse.CrezyBackend.domain.admin.reportManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.reportManage.service.AdminReportService;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportResponseForm;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.admin.reportManage.controller.form.ReportReadResponseForm;
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

}
