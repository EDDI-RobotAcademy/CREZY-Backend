package me.muse.CrezyBackend.domain.playlist.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PlaylistResponseForm {
    final private Long playlistId;
    final private String playlistName;
    final private String writer;
    final private int likeCount;
    final private int songCount;
    final private String thumbnailName;
}
