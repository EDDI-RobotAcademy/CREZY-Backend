package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminSongModifyLyricsRequestForm {
    final private Long songId;
    final private String lyrics;
}
