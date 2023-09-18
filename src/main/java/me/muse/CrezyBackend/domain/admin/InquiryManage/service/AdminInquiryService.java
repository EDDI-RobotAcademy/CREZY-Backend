package me.muse.CrezyBackend.domain.admin.InquiryManage.service;

import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface AdminInquiryService {
    InquiryCountResponseForm countInquiry(HttpHeaders headers);
    Page<AdminInquiryListResponseForm> list(HttpHeaders headers, AdminInquiryListRequestForm requestForm);
    public List<AdminInquiryListResponseForm> waitingInquiryList(HttpHeaders headers);

    AdminInquiryReadResponseForm adminReadInquiry(HttpHeaders headers, Long inquiryDetailId);
}
