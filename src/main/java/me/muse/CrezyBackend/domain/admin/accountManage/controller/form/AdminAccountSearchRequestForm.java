package me.muse.CrezyBackend.domain.admin.accountManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminAccountSearchRequestForm {
    final private int page;
    final private String keyword;
}
