package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportReadSongResponseForm {
    private String reporterProfileName;
    private String reportedProfileName;
    private String playlistName;
    private String title;
    private String singer;
    private String link;
    private String lyrics;
    private String ReportedCategoryType;
    private Long reportedSongId;

    public ReportReadSongResponseForm(String title) {
        this.title = title;
    }
}
