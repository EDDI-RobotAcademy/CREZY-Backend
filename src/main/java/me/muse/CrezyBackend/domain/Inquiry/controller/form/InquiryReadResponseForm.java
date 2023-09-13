package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.Inquiry.entity.Inquiry;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategory;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategoryType;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryImages;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InquiryReadResponseForm {
    private String inquiryTitle;
    private String inquiryContent;
    private List<String> inquiryImageNames = new ArrayList<>();

    public InquiryReadResponseForm(String inquiryTitle, String inquiryContent, List<InquiryImages> inquiryImageList) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;

        for (InquiryImages images: inquiryImageList) {
            this.inquiryImageNames.add(images.getInquiryImagePath());
        }
    }
}
