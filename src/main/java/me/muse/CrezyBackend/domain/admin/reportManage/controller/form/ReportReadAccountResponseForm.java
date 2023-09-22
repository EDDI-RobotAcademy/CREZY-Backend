package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportReadAccountResponseForm {
    private String reporterProfileName;
    private String reportedProfileName;
    private String reportedProfileImageName;
    private String ReportedCategoryType;
    private int reportedCounts;
    private int warningCounts;
    private int inquiryCounts;
    private Long reportedAccountId;

    public ReportReadAccountResponseForm(String reportedProfileName) {
        this.reportedProfileName = reportedProfileName;
    }
}
