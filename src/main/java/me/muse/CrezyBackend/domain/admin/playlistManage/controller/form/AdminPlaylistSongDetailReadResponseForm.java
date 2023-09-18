package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistSongDetailReadResponseForm {
    final private Long songId;
    final private String title;
    final private String singer;
    final private LocalDate CreateDate;
}
