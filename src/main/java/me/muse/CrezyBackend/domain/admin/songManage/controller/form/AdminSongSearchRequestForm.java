package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminSongSearchRequestForm {
    final private int page;
    final private String keyword;
}
