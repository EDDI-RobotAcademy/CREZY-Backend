package me.muse.CrezyBackend.domain.admin.reportManage.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCountResponseForm {
    private int approveCount;
    private int returnCount;
    private int holdonCount;
    private int totalCount;
}
