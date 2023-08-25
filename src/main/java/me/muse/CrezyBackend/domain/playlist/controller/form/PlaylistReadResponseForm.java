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
    private List<Song> songlist;

    @Builder
    public PlaylistReadResponseForm(String playlistName, String accountWriter, String thumbnailName, List<Song> songlist) {
        this.playlistName = playlistName;
        this.accountWriter = accountWriter;
        this.thumbnailName = thumbnailName;
        this.songlist = songlist;
    }
}
