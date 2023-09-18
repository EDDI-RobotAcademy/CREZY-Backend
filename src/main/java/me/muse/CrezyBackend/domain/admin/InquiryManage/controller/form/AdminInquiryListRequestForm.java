package me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminInquiryListRequestForm {
    final private String statusType;
    final private String categoryType;
    final private Integer page;
}
