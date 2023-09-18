package me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryImages;
import me.muse.CrezyBackend.domain.account.entity.Profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class AdminInquiryReadResponseForm {
    private Long inquiryDetailId;
    private String inquiryTitle;
    private String inquiryContent;
    private String nickname;
    private String inquiryCategoryType;
    private LocalDate createInquiryDate;
    private List<String> inquiryImageNames = new ArrayList<>();

    public AdminInquiryReadResponseForm(Long inquiryDetailId, String inquiryTitle, String inquiryContent, String nickname, String inquiryCategoryType, LocalDate createInquiryDate, List<InquiryImages> inquiryImageList) {
        this.inquiryDetailId = inquiryDetailId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.nickname = nickname;
        this.inquiryCategoryType = inquiryCategoryType;
        this.createInquiryDate = createInquiryDate;

        for (InquiryImages images: inquiryImageList) {
            this.inquiryImageNames.add(images.getInquiryImagePath());
        }
    }
}
