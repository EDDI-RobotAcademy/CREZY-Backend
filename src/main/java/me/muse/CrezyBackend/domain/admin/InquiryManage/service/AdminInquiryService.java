package me.muse.CrezyBackend.domain.admin.InquiryManage.service;

import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.InquiryCountResponseForm;
import org.springframework.http.HttpHeaders;

public interface AdminInquiryService {
    InquiryCountResponseForm countInquiry(HttpHeaders headers);

}
