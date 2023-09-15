package me.muse.CrezyBackend.domain.Inquiry.service;

import me.muse.CrezyBackend.domain.Inquiry.controller.form.*;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface InquiryService {
    long register(InquiryRegisterRequestForm requestForm, HttpHeaders headers);
    List<InquiryListResponseForm> list(HttpHeaders headers);
    InquiryReadResponseForm read(Long inquiryId, HttpHeaders headers);
    InquiryModifyResponseForm modify(InquiryModifyRequestForm requestForm, HttpHeaders headers);
}
