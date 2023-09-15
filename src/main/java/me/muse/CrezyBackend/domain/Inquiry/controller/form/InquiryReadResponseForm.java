package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.Getter;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryImages;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InquiryReadResponseForm {
    private Long inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private List<String> inquiryImageNames = new ArrayList<>();

    public InquiryReadResponseForm(Long inquiryId, String inquiryTitle, String inquiryContent, List<InquiryImages> inquiryImageList) {
        this.inquiryId = inquiryId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;

        for (InquiryImages images: inquiryImageList) {
            this.inquiryImageNames.add(images.getInquiryImagePath());
        }
    }
}
