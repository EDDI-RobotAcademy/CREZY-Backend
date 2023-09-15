package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InquiryCountResponseForm {
    final private int todayInquiryCount;
    final private int waitingAnswerInquiryCount;
    final private long totalInquiryCount;
}
