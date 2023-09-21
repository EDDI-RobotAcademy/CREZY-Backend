package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.Getter;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryImages;
import me.muse.CrezyBackend.domain.admin.InquiryManage.Entity.InquiryAnswer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InquiryReadResponseForm {
    private Long inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private InquiryAnswer inquiryAnswer;
    private List<String> inquiryImageNames = new ArrayList<>();

    public InquiryReadResponseForm(Long inquiryId, String inquiryTitle, String inquiryContent,
                                   InquiryAnswer inquiryAnswer, List<InquiryImages> inquiryImageList) {
        this.inquiryId = inquiryId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryAnswer = inquiryAnswer;

        for (InquiryImages images: inquiryImageList) {
            this.inquiryImageNames.add(images.getInquiryImagePath());
        }
    }
}
