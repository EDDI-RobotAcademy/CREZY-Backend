package me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class AdminInquiryListResponseForm {
    final private Long inquiryId;
    final private String inquiryTitle;
    final private String nickname;
    final private LocalDate createInquiryDate;
    final private String inquiryCategoryType;
    final private boolean existAnswer;
}
