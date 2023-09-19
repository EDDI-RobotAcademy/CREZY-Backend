package me.muse.CrezyBackend.domain.song.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SongRegisterRequestForm {
    final private Long playlistId;
    final private String title;
    final private String singer;
    final private String link;
    final private String lyrics;
}
