package me.muse.CrezyBackend.domain.admin.accountManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminAccountListRequestForm {
    final private Integer warningCounts;
    final private Integer page;
}
