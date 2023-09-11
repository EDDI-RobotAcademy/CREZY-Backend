package me.muse.CrezyBackend.domain.report.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.report.entity.Report;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ReportReadResponseForm {
    final private Report report;
    final private String reportContent;
    final private String reporterProfileName;
    final private String reportedProfileName;
    final private LocalDate createReportDate;
}
