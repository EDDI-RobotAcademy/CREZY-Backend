package me.muse.CrezyBackend.domain.Inquiry.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class InquiryRegisterRequestForm {
    final private String inquiryCategoryType;
    final private String inquiryTitle;
    final private String inquiryContent;
    final private List<String > inquiryImageNames;
}
