package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.SongStatusType;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminSongListResponseForm {
    final private Long songId;
    final private String title;
    final private String singer;
    final private String nickname;
    final private LocalDate createDate;
    final private SongStatusType songStatusType;
}
