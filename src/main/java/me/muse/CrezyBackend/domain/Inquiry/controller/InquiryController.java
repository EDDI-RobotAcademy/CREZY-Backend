package me.muse.CrezyBackend.domain.Inquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryRegisterRequestForm;
import me.muse.CrezyBackend.domain.Inquiry.service.InquiryService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/inquiry")
@RestController
public class InquiryController {

    final private InquiryService inquiryService;

    @PostMapping("/register") // 플레이 리스트 등록
    public long inquiryRegister (@RequestBody InquiryRegisterRequestForm requestForm, @RequestHeader HttpHeaders headers) {
        log.info("inquiryRegister()");
        return inquiryService.register(requestForm, headers);
    }
}