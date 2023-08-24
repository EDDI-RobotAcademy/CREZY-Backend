package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PlaylistModifyRequestForm {
    final private Long playlistId;
    final private String playlistName;
    final private String writer;
    final private String thumbnailName;
}
