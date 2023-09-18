package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistReadResponseForm {
    final private String playlistName;
    final private String playlistWriter;
    final private String thumbnailName;
    final private LocalDate playlistCreateDate;
    final private Integer likeCounts;
    final private Integer songCounts;
    final private List<AdminPlaylistSongDetailReadResponseForm> songDetail;
}
