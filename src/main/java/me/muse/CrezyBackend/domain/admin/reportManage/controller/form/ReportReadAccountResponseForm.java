package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReportReadAccountResponseForm {
    final private String reporterProfileName;
    final private String reportedProfileName;
    final private String reportedProfileImageName;
}
