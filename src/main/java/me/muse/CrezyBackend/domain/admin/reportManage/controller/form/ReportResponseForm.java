package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseForm {
    private Long reportId;
    private String reporterNickname;
    private Long reportedId;
    private String reportContent;
    private String reportedCategoryType;
    private String reportStatusType;
    private LocalDate createReportDate;
    private Integer SongReportCount;
    private Integer PlaylistReportCount;
    private Integer AccountReportCount;

    public ReportResponseForm(String reportContent) {
        this.reportContent = reportContent;
    }
}
