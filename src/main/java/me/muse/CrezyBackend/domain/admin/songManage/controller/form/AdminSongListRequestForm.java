package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminSongListRequestForm {
    final private String songStatusType;
    final private String sortType;
    final private Integer page;
}
