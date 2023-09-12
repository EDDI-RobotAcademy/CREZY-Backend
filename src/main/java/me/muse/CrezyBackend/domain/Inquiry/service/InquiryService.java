package me.muse.CrezyBackend.domain.Inquiry.service;

import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryRegisterRequestForm;
import org.springframework.http.HttpHeaders;

public interface InquiryService {
    long register(InquiryRegisterRequestForm requestForm, HttpHeaders headers);
}
