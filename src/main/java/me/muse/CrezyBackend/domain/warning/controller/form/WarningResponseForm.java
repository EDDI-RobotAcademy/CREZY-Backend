package me.muse.CrezyBackend.domain.warning.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class WarningResponseForm {
    final private Long warningId;
    final private LocalDate createWarningDate;
    final private String reportedCategoryType;
    final private String reportContent;
    final private LocalDate createReportDate;
}
