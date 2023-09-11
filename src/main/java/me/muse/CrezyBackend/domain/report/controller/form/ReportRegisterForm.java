package me.muse.CrezyBackend.domain.report.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public class ReportRegisterForm {
    final private String reportedCategoryType;
    final private String reportContent;
    final private Long reportedAccountId;

}
