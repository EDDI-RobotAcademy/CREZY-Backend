package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MyPlaylistResponseForm {
    final private Long playlistId;
    final private String playlistName;
    final private int likeCount;
    final private int songCount;
    final private String thumbnailName;
}
