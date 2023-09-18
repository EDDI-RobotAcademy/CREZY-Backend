package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistReadResponseForm {
    final private Integer likeCounts;
    final private Integer songCounts;
    final private List<Song> songlist;
}
