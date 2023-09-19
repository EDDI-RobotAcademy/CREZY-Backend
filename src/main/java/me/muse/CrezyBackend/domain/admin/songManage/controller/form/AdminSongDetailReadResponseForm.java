package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminSongDetailReadResponseForm {
    private Long songId;
    private String title;
    private String singer;
    private LocalDate createDate;
    private String link;
    private String lyrics;
    private String blockedDate;
    private String songStatus;

    public AdminSongDetailReadResponseForm(Long songId, String title, String singer, LocalDate createDate, String link, String lyrics, String blockedDate, String songStatus) {
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        this.createDate = createDate;
        this.link = link;
        this.lyrics = lyrics;
        this.blockedDate = blockedDate;
        this.songStatus = songStatus;
    }

    public AdminSongDetailReadResponseForm(Long songId, String title, String singer, LocalDate createDate, String songStatus) {
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        this.createDate = createDate;
        this.songStatus = songStatus;
    }
}
