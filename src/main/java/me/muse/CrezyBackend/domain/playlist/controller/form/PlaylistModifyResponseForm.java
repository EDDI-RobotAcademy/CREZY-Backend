package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PlaylistModifyResponseForm {
    final private Long playlistId;
    final private String playlistName;
    final private String thumbnailName;
}
