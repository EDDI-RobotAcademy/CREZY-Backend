package me.muse.CrezyBackend.domain.Inquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.*;
import me.muse.CrezyBackend.domain.Inquiry.service.InquiryService;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistModifyRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistModifyResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
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

    @GetMapping("/{inquiryId}")
    public InquiryReadResponseForm readInquiry(@PathVariable("inquiryId") Long inquiryId, @RequestHeader HttpHeaders headers) {
        log.info("readInquiry()");
        return inquiryService.read(inquiryId, headers);
    }

    @PostMapping("/modify") // 1:1 문의 수정
    public InquiryModifyResponseForm modifyInquiry(@RequestBody InquiryModifyRequestForm requestForm, @RequestHeader HttpHeaders headers){
        log.info("modifyInquiry()");
        return inquiryService.modify(requestForm, headers);
    }

    @DeleteMapping("/{inquiryId}") // 플레이 리스트 삭제
    public boolean deleteInquiry(@PathVariable("inquiryId") Long inquiryId, @RequestHeader HttpHeaders headers) {
        log.info("deleteInquiry()");
        return inquiryService.delete(inquiryId, headers);
    }
}