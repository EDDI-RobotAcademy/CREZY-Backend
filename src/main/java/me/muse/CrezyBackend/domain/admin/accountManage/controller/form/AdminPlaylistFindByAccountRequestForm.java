package me.muse.CrezyBackend.domain.admin.accountManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistFindByAccountRequestForm {
    final private Long accountId;
    final private Integer page;
}
