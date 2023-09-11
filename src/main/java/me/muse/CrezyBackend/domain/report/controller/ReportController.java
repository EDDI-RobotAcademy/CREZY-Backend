package me.muse.CrezyBackend.domain.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.report.controller.form.ReportResponseForm;
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
    public List<ReportResponseForm> reportList(@RequestParam("page") Integer page, @RequestHeader HttpHeaders headers){
        log.info("reportList()");
        return reportService.list(page,headers);
    }
   @GetMapping("/list/total-page")
    public Integer getTotalPage(){
        return reportService.getTotalPage();
    }
}
