package me.muse.CrezyBackend.domain.admin.InquiryManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.Inquiry.entity.Inquiry;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategory;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategoryType;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryDetail;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryCategoryTypeRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryDetailRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryRepository;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryListRequestForm;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryListResponseForm;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.AdminInquiryReadResponseForm;
import me.muse.CrezyBackend.domain.admin.InquiryManage.controller.form.InquiryCountResponseForm;
import me.muse.CrezyBackend.utility.TransformToDate.TransformToDate;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminInquiryServiceImpl implements AdminInquiryService {
    final private InquiryRepository inquiryRepository;
    final private InquiryDetailRepository inquiryDetailRepository;
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private InquiryCategoryTypeRepository inquiryCategoryTypeRepository;

    @Override
    public InquiryCountResponseForm countInquiry(HttpHeaders headers) {
        if (!checkAdmin(headers)) return null;

        Long date = System.currentTimeMillis();

        SimpleDateFormat sdt = new SimpleDateFormat();
        sdt.applyPattern("yyyy-MM-dd");
        String currentDate = sdt.format(date);

        LocalDate transformCurrentDate = TransformToDate.transformToDate(currentDate);
        int todayInquiryCount = inquiryRepository.countByCreateInquiryDate(transformCurrentDate);
        int waitingAnswerInquiryCount = inquiryRepository.countWaitingAnswer();
        long totalInquiryCount = inquiryRepository.count();

        return new InquiryCountResponseForm(todayInquiryCount, waitingAnswerInquiryCount, totalInquiryCount);
    }

    private boolean checkAdmin(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getRoleType().getRoleType() != ADMIN) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Page<AdminInquiryListResponseForm> list(HttpHeaders headers, AdminInquiryListRequestForm requestForm) {
        if (!checkAdmin(headers)) return null;

        List<InquiryDetail> inquiryDetailList = new ArrayList<>();

        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10);

        if(requestForm.getCategoryType().equals("TOTAL")){
          switch (requestForm.getStatusType()){
              case "total" -> inquiryDetailList = inquiryDetailRepository.findAllDetailWithAnswer();
              case "today" -> inquiryDetailList = inquiryDetailRepository.findByInquiry_CreateInquiryDate(LocalDate.now());
              case "waiting" -> inquiryDetailList = inquiryDetailRepository.findWaitingAnswer();
          }
        }else {
            InquiryCategoryType inquiryCategoryType = inquiryCategoryTypeRepository.findByInquiryCategory(InquiryCategory.valueOf(requestForm.getCategoryType()))
                    .orElseThrow(() -> new IllegalArgumentException("InquiryCategoryType not found"));

            switch (requestForm.getStatusType()){
                case "total" -> inquiryDetailList = inquiryDetailRepository.findAllDetailWithAnswerByInquiryCategoryType(inquiryCategoryType);
                case "today" -> inquiryDetailList = inquiryDetailRepository.findByInquiry_CreateInquiryDateAndInquiry_InquiryCategoryType(LocalDate.now(), inquiryCategoryType);
                case "waiting" -> inquiryDetailList = inquiryDetailRepository.findWaitingAnswerByInquiryCategoryType(inquiryCategoryType);
            }
        }

        inquiryDetailList.sort(Comparator.comparing(id -> id.getInquiry().getCreateInquiryDate(), Comparator.nullsLast(Comparator.reverseOrder())));

        List<AdminInquiryListResponseForm> responseFormList = new ArrayList<>();

        for (InquiryDetail inquiryDetail : inquiryDetailList) {
            Inquiry inquiry = inquiryDetail.getInquiry();

            AdminInquiryListResponseForm responseForm = new AdminInquiryListResponseForm(
                    inquiry.getInquiryId(),
                    inquiryDetail.getInquiryTitle(),
                    inquiryDetail.getProfile().getNickname(),
                    inquiry.getCreateInquiryDate(),
                    inquiry.getInquiryCategoryType().getInquiryCategory().toString(),
                    isExistAnswer(inquiry));

            responseFormList.add(responseForm);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseFormList.size());

        return new PageImpl<>(
                responseFormList.subList(start, end),
                pageable,
                responseFormList.size()
        );
    }

    private boolean isExistAnswer(Inquiry inquiry){
        return inquiry.getInquiryAnswer() != null;
    }

    @Override
    public List<AdminInquiryListResponseForm> waitingInquiryList(HttpHeaders headers){
        if (!checkAdmin(headers)) return null;

        List<AdminInquiryListResponseForm> responseFormList = new ArrayList<>();

        List<InquiryDetail> userInquiryDetails = inquiryDetailRepository.findOldestUnansweredInquiries();

        for (InquiryDetail inquiryDetail : userInquiryDetails) {
            Inquiry inquiry = inquiryDetail.getInquiry();

            AdminInquiryListResponseForm responseForm = new AdminInquiryListResponseForm(
                    inquiry.getInquiryId(),
                    inquiryDetail.getInquiryTitle(),
                    inquiryDetail.getProfile().getNickname(),
                    inquiry.getCreateInquiryDate(),
                    inquiry.getInquiryCategoryType().getInquiryCategory().toString(),
                    isExistAnswer(inquiry));

            responseFormList.add(responseForm);
        }

        return responseFormList;
    }

    @Override
    @Transactional
    public AdminInquiryReadResponseForm adminReadInquiry(HttpHeaders headers, Long inquiryId) {
        if (!checkAdmin(headers)) return null;

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("InquiryDetail not found"));

        InquiryDetail inquiryDetail = inquiryDetailRepository.findByInquiryId(inquiry.getInquiryId())
                .orElseThrow(() -> new IllegalArgumentException("InquiryDetail not found"));

        return new AdminInquiryReadResponseForm(
                inquiryDetail.getInquiryDetailId(),
                inquiryDetail.getInquiryTitle(),
                inquiryDetail.getInquiryContent(),
                inquiryDetail.getProfile().getNickname(),
                inquiryDetail.getInquiry().getInquiryCategoryType().getInquiryCategory().toString(),
                inquiryDetail.getInquiry().getCreateInquiryDate(),
                inquiryDetail.getInquiryImageNames()
        );
    }
}
