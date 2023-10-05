package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReportListRequestForm {
    final private String categoryType;
    final private String statusType;
    final private Integer page;
}
