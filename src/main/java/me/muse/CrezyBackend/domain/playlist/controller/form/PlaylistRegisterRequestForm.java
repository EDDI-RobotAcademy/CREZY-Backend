package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;

@RequiredArgsConstructor
@Getter
public class PlaylistRegisterRequestForm {
    final private String playlistName;
    final private String thumbnailName;
}
