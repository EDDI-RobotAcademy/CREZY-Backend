package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TodayStatusSongResponseForm {
    final private Integer todaySong;
    final private Integer totalSong;
    final private Integer increaseRate;
    final private List<Integer> songCounts;
    final private List<String> songDateList;
}
