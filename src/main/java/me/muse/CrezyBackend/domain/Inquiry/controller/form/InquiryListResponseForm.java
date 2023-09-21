package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategoryType;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class InquiryListResponseForm {
     final private Long inquiryId;
     final private InquiryCategoryType inquiryCategoryType;
     final private String inquiryTitle;
     final private LocalDate createInquiryDate;
     final private boolean existAnswer;
}
