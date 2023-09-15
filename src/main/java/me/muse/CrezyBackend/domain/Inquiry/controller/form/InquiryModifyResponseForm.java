package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.Getter;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryImages;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InquiryModifyResponseForm {
    final private Long inquiryId;
    final private String inquiryTitle;
    final private String inquiryContent;
    final private List<String> inquiryImageNames = new ArrayList<>();

    public InquiryModifyResponseForm(
            Long inquiryId, String inquiryTitle, String inquiryContent, List<InquiryImages> inquiryImagesList) {
        this.inquiryId = inquiryId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;

        for (InquiryImages inquiryImages: inquiryImagesList) {
            this.inquiryImageNames.add(inquiryImages.getInquiryImagePath());
        }
    }
}
