package me.muse.CrezyBackend.domain.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.report.controller.form.ReportProcessingForm;
import me.muse.CrezyBackend.domain.report.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/report")
@RestController
public class ReportController {
    final private ReportService reportService;

    @PostMapping("/processing")
    public boolean approveReport(@RequestBody ReportProcessingForm processingForm, @RequestHeader HttpHeaders headers) {
        log.info("approveReport()");
        return reportService.processingReport(processingForm, headers);
    }
}
