package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportReadPlaylistResponseForm {
    private String reporterProfileName;
    private String reportedProfileName;
    private String playlistName;
    private String thumbnailName;
    private String ReportedCategoryType;
    private int likeCounts;
    private int songCounts;
    private Long reportedPlaylistId;

    public ReportReadPlaylistResponseForm(String playlistName) {
        this.playlistName = playlistName;
    }
}
