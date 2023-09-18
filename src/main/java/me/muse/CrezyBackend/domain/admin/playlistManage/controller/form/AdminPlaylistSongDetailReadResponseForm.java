package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistSongDetailReadResponseForm {
    private Long songId;
    private String title;
    private String singer;
    private LocalDate createDate;
    private String link;
    private String lyrics;
    private String blockedDate;

    public AdminPlaylistSongDetailReadResponseForm(Long songId, String title, String singer, LocalDate createDate, String link, String lyrics, String blockedDate) {
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        this.createDate = createDate;
        this.link = link;
        this.lyrics = lyrics;
        this.blockedDate = blockedDate;
    }

    public AdminPlaylistSongDetailReadResponseForm(Long songId, String title, String singer, LocalDate createDate) {
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        this.createDate = createDate;
    }
}
