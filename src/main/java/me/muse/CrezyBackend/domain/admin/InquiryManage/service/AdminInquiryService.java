package me.muse.CrezyBackend.domain.admin.InquiryManage.service;

import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryListResponseForm;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.InquiryCountResponseForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface AdminInquiryService {
    InquiryCountResponseForm countInquiry(HttpHeaders headers);
    List<AdminInquiryListResponseForm> list(HttpHeaders headers);
    public List<AdminInquiryListResponseForm> waitingInquiryList(HttpHeaders headers);
}
