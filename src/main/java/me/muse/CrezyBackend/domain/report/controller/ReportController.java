package me.muse.CrezyBackend.domain.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.report.controller.form.ReportReadResponseForm;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.controller.form.ReportResponseForm;
import me.muse.CrezyBackend.domain.report.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.report.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    final private ReportService reportService;

    @GetMapping(value = "/list")
    public List<ReportResponseForm> reportList(@RequestParam("page") Integer page, @RequestHeader HttpHeaders headers) {
        log.info("reportList()");
        return reportService.list(page, headers);
    }

    @GetMapping("/list/total-page")
    public Integer getTotalPage() {
        return reportService.getTotalPage();
    }

    @PostMapping("/processing")
    public boolean approveReport(@RequestBody ReportProcessingForm processingForm, @RequestHeader HttpHeaders headers) {
        log.info("approveReport()");
        return reportService.processingReport(processingForm, headers);

    }

    @GetMapping("/read-report")
    public ReportReadResponseForm readReport(@RequestParam("reportId") Long reportId, @RequestHeader HttpHeaders headers) {
        log.info("readReport()");
        return reportService.readReport(reportId,headers);
    }

    @PostMapping("/register-report")
    public long registerReport(@RequestBody ReportRegisterForm reportRegisterForm, @RequestHeader HttpHeaders headers) {
        log.info("registerReport()");
        return reportService.registerReport(reportRegisterForm, headers);
    }
}