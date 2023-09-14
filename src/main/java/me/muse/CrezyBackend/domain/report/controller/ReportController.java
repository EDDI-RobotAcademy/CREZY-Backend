package me.muse.CrezyBackend.domain.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.report.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    final private ReportService reportService;

    @PostMapping("/register-report")
    public long registerReport(@RequestBody ReportRegisterForm reportRegisterForm, @RequestHeader HttpHeaders headers) {
        log.info("registerReport()");
        return reportService.registerReport(reportRegisterForm, headers);
    }
}