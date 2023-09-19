package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class TodayStatusPlaylistResponseForm {
    final private Integer todayPlaylist;
    final private Integer totalPlaylist;
    final private Integer increaseRate;
    final private List<Integer> playlistCounts;
    final private List<String> playlistDateList;
}
