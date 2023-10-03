package me.muse.CrezyBackend.domain.song.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class SongOrderRequestForm {
    final private Long playlistId;
    final private List<Integer> songIndexList;
}
