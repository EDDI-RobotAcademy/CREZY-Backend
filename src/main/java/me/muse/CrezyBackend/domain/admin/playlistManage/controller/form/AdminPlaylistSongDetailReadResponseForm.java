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
    private LocalDate CreateDate;
    private String link;
    private String lyrics;

    public AdminPlaylistSongDetailReadResponseForm(Long songId, String title, String singer, LocalDate createDate, String link, String lyrics) {
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        CreateDate = createDate;
        this.link = link;
        this.lyrics = lyrics;
    }

    public AdminPlaylistSongDetailReadResponseForm(Long songId, String title, String singer, LocalDate createDate) {
        this.songId = songId;
        this.title = title;
        this.singer = singer;
        CreateDate = createDate;
    }
}
