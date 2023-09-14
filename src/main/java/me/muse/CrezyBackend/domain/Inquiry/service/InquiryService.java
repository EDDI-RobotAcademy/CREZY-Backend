package me.muse.CrezyBackend.domain.Inquiry.service;

import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryListResponseForm;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryReadResponseForm;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryRegisterRequestForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface InquiryService {
    long register(InquiryRegisterRequestForm requestForm, HttpHeaders headers);
    List<InquiryListResponseForm> list(HttpHeaders headers);
    InquiryReadResponseForm read(Long inquiryId);
}
