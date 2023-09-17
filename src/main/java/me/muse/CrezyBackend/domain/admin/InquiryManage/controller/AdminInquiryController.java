package me.muse.CrezyBackend.domain.admin.InquiryManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryListResponseForm;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.InquiryCountResponseForm;
import me.muse.CrezyBackend.domain.admin.InquiryManage.service.AdminInquiryService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-inquiry")
public class AdminInquiryController {
    final private AdminInquiryService adminInquiryService;
    @GetMapping("/counting-inquiry")
    public InquiryCountResponseForm countingInquiry(@RequestHeader HttpHeaders headers) {
        return adminInquiryService.countInquiry(headers);
    }

    @GetMapping("/inquiry-list")
    public List<AdminInquiryListResponseForm> adminInquiryList(@RequestHeader HttpHeaders headers){
        return adminInquiryService.list(headers);
    }

    @GetMapping("/waiting-inquiry-list")
    public List<AdminInquiryListResponseForm> waitingInquiryList(@RequestHeader HttpHeaders headers){
        return adminInquiryService.waitingInquiryList(headers);
    }
}
