package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistsRequestForm {
    final private String sortType;
    final private Integer page;
}
