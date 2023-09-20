package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.report.entity.ReportStatus;

@RequiredArgsConstructor
@Getter
public class ReportProcessingForm {
    final private Long reportId;
    final private String reportStatus;
}
