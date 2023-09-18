package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReportReadPlaylistResponseForm {
    final private String reporterProfileName;
    final private String reportedProfileName;
    final private String playlistName;
    final private String thumbnailName;
    final private String ReportedCategoryType;
}
