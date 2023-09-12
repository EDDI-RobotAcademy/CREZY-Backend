package me.muse.CrezyBackend.domain.Inquiry.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.Inquiry.controller.form.InquiryRegisterRequestForm;
import me.muse.CrezyBackend.domain.Inquiry.entity.*;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryCategoryTypeRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryDetailRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryImagesRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryRepository;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategory;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategoryType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private InquiryCategoryTypeRepository inquiryCategoryTypeRepository;
    final private InquiryRepository inquiryRepository;
    final private InquiryDetailRepository inquiryDetailRepository;
    final private InquiryImagesRepository inquiryImagesRepository;
    final private ProfileRepository profileRepository;

    @Override
    @Transactional
    public long register(InquiryRegisterRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return -1;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Optional<Profile> maybeProfile = profileRepository.findByAccount(account);
        if (maybeProfile.isEmpty()) {
            return -1;
        }

        InquiryCategoryType inquiryCategoryType = inquiryCategoryTypeRepository.findByInquiryCategory(InquiryCategory.valueOf(requestForm.getInquiryCategoryType())).get();
        inquiryCategoryTypeRepository.save(inquiryCategoryType);

        List<String> inquiryImageUrls = requestForm.getInquiryImageNames();
        final List<InquiryImages> inquiryImagesList = new ArrayList<>();

        final Inquiry inquiry = new Inquiry(inquiryCategoryType);
        final InquiryDetail inquiryDetail = new InquiryDetail(requestForm.getInquiryTitle(), requestForm.getInquiryContent(), maybeProfile.get());

        for (String imageUrl : inquiryImageUrls) {
            InquiryImages inquiryImage = new InquiryImages(imageUrl);
            inquiryImage.setInquiryDetail(inquiryDetail);
            inquiryImagesList.add(inquiryImage);
        }

        inquiryImagesRepository.saveAll(inquiryImagesList);
        inquiryDetailRepository.save(inquiryDetail);
        inquiryRepository.save(inquiry);

        return inquiry.getInquiryId();
    }
}
