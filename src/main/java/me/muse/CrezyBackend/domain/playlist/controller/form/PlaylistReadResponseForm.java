package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.util.List;


@Getter
public class PlaylistReadResponseForm {
    private String playlistName;
    private String accountWriter;
    private String thumbnailName;
    private int likes;
    private List<Song> songlist;

    @Builder
    public PlaylistReadResponseForm(String playlistName, String accountWriter, String thumbnailName, int likes, List<Song> songlist) {
        this.playlistName = playlistName;
        this.accountWriter = accountWriter;
        this.thumbnailName = thumbnailName;
        this.likes = likes;
        this.songlist = songlist;
    }
}
