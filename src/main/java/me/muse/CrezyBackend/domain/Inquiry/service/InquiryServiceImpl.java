package me.muse.CrezyBackend.domain.Inquiry.service;

import me.muse.CrezyBackend.domain.Inquiry.controller.form.*;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistModifyResponseForm;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.Inquiry.entity.*;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryCategoryTypeRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryDetailRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryImagesRepository;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryRepository;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
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
        final InquiryDetail inquiryDetail = new InquiryDetail(requestForm.getInquiryTitle(),
                requestForm.getInquiryContent(), maybeProfile.get(), inquiry);

        for (String imageUrl : inquiryImageUrls) {
            InquiryImages inquiryImage = new InquiryImages(imageUrl);
            inquiryImage.setInquiryDetail(inquiryDetail);
            inquiryImagesList.add(inquiryImage);
        }

        inquiryImagesRepository.saveAll(inquiryImagesList);
        inquiryDetailRepository.save(inquiryDetail);
        inquiryRepository.save(inquiry);

        return inquiryDetail.getInquiryDetailId();
    }

    @Override
    @Transactional
    public List<InquiryListResponseForm> list(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return new ArrayList<>();
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Optional<Profile> maybeProfile = profileRepository.findByAccount(account);
        if (maybeProfile.isEmpty()) {
            return null;
        }

        List<InquiryListResponseForm> inquiryListResponseForms = new ArrayList<>();

        List<InquiryDetail> userInquiryDetails = inquiryDetailRepository.findByProfile(maybeProfile.get());

        for (InquiryDetail inquiryDetail : userInquiryDetails) {
            Inquiry inquiry = inquiryDetail.getInquiry();
            InquiryListResponseForm responseForm = new InquiryListResponseForm(
                    inquiry.getInquiryId(), inquiry.getInquiryCategoryType(),
                    inquiryDetail.getInquiryTitle(), inquiry.getCreateInquiryDate());

            inquiryListResponseForms.add(responseForm);
        }

        return inquiryListResponseForms;
    }

    @Override
    @Transactional
    public InquiryReadResponseForm read(Long inquiryId, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Optional<InquiryDetail> maybeInquiryDetail = inquiryDetailRepository.findByInquiryId(inquiryId);
        if (maybeInquiryDetail.isEmpty()) {
            return null;
        }

        final InquiryDetail inquiryDetail = maybeInquiryDetail.get();
        final List<InquiryImages> inquiryImagesList = inquiryImagesRepository.findByInquiryDetailId(inquiryDetail.getInquiryDetailId());

        InquiryReadResponseForm responseForm = new InquiryReadResponseForm(inquiryDetail.getInquiry().getInquiryId(),
                inquiryDetail.getInquiryTitle(), inquiryDetail.getInquiryContent(), inquiryImagesList);

        return responseForm;
    }

    @Override
    @Transactional
    public InquiryModifyResponseForm modify(InquiryModifyRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if (maybeAccount.isEmpty()) {
            return null;
        }

        Optional<InquiryDetail> maybeInquiryDetail = inquiryDetailRepository.findByInquiryId(requestForm.getInquiryId());
        if (maybeInquiryDetail.isEmpty()) {
            return null;
        }

        InquiryDetail inquiryDetail = maybeInquiryDetail.get();
        inquiryDetail.setInquiryTitle(requestForm.getInquiryTitle());
        inquiryDetail.setInquiryContent(requestForm.getInquiryContent());
        inquiryDetailRepository.save(inquiryDetail);

        // 기존 이미지 삭제
        inquiryImagesRepository.deleteAll(inquiryDetail.getInquiryImageNames());

        final List<InquiryImages> inquiryImagesList = new ArrayList<>();

        for (String inquiryImageName : requestForm.getInquiryImageNames()) {
            InquiryImages inquiryImages = new InquiryImages(inquiryImageName);
            inquiryImages.setInquiryDetail(inquiryDetail);
            inquiryImagesList.add(inquiryImages);
        }

        inquiryImagesRepository.saveAll(inquiryImagesList);

        return new InquiryModifyResponseForm(inquiryDetail.getInquiry().getInquiryId(),
                inquiryDetail.getInquiryTitle(), inquiryDetail.getInquiryContent(), inquiryImagesList);
    }

    @Override
    @Transactional
    public boolean delete(Long inquiryId, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if (maybeAccount.isEmpty()) {
            return false;
        }

        Optional<InquiryDetail> maybeInquiryDetail = inquiryDetailRepository.findByInquiryId(inquiryId);
        if (maybeInquiryDetail.isEmpty()) {
            return false;
        }

        InquiryDetail inquiryDetail = maybeInquiryDetail.get();

        inquiryImagesRepository.deleteAllByInquiryDetailId(inquiryDetail.getInquiryDetailId());
        inquiryRepository.deleteById(inquiryId);
        inquiryDetailRepository.deleteById(inquiryDetail.getInquiryDetailId());

        return true;
    }
}