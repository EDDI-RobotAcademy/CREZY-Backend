package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
public class PlaylistReadResponseForm {
    private String playlistName;
    private String accountWriter;
    private String thumbnailName;
    private String title;
    private String singer;

    @Builder
    public PlaylistReadResponseForm(String playlistName, String accountWriter, String thumbnailName, String title, String singer) {
        this.playlistName = playlistName;
        this.accountWriter = accountWriter;
        this.thumbnailName = thumbnailName;
        this.title = title;
        this.singer = singer;
    }

    public PlaylistReadResponseForm(String playlistName, String writer, String thumbnailName, List<PlaylistReadResponseForm> list) {
    }
}
