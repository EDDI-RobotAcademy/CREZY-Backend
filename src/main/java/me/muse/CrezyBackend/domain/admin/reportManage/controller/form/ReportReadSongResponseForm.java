package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReportReadSongResponseForm {
    final private String reporterProfileName;
    final private String reportedProfileName;
    final private String playlistName;
    final private String title;
    final private String singer;
    final private String link;
    final private String lyrics;
    final private String ReportedCategoryType;
    final private Long reportedSongId;
}
