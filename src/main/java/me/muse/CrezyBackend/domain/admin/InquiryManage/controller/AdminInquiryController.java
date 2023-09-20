package me.muse.CrezyBackend.domain.admin.InquiryManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.*;
import me.muse.CrezyBackend.domain.admin.InquiryManage.service.AdminInquiryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/inquiry-list")
    public Page<AdminInquiryListResponseForm> adminInquiryList(@RequestHeader HttpHeaders headers, @RequestBody AdminInquiryListRequestForm requestForm){
        return adminInquiryService.list(headers, requestForm);
    }

    @GetMapping("/waiting-inquiry-list")
    public List<AdminInquiryListResponseForm> waitingInquiryList(@RequestHeader HttpHeaders headers){
        return adminInquiryService.waitingInquiryList(headers);
    }

    @GetMapping("/{inquiryId}")
    public AdminInquiryReadResponseForm adminReadInquiry(@RequestHeader HttpHeaders headers, @PathVariable("inquiryId") Long inquiryId){
        return adminInquiryService.adminReadInquiry(headers, inquiryId);
    }

    @PostMapping("/inquiry-answer")
    public Long inquiryAnswerRegist(@RequestHeader HttpHeaders headers, @RequestBody AdminInquiryAnswerRegisterForm registerForm){
        return adminInquiryService.registAnswer(headers, registerForm);
    }
}
