package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Getter
@RequiredArgsConstructor
public class ReportResponseForm {
    final private Long reportId;
    final private Long reportedId;
    final private String reportContent;
    final private String reportedCategoryType;
    final private String reportStatusType;
    final private LocalDate createReportDate;
    final private Integer SongReportCount;
    final private Integer PlaylistReportCount;
    final private Integer AccountReportCount;

}
