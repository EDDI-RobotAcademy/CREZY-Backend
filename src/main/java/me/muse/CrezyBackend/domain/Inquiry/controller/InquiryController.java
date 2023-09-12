package me.muse.CrezyBackend.domain.Inquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryListResponseForm;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryRegisterRequestForm;
import me.muse.CrezyBackend.domain.Inquiry.service.InquiryService;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/inquiry")
@RestController
public class InquiryController {

    final private InquiryService inquiryService;

    @PostMapping("/register") // 1:1 문의 등록
    public long inquiryRegister (@RequestBody InquiryRegisterRequestForm requestForm, @RequestHeader HttpHeaders headers) {
        log.info("inquiryRegister()");
        return inquiryService.register(requestForm, headers);
    }

    @GetMapping("/list") // 1:1 문의 리스트
    public List<InquiryListResponseForm> inquiryList(@RequestHeader HttpHeaders headers){
        log.info("inquiryList()");
        return inquiryService.list(headers);
    }
}