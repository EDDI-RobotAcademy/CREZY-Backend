package me.muse.CrezyBackend.domain.Inquiry.repository;

import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategory;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategoryType;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryDetail;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InquiryDetailRepository extends JpaRepository<InquiryDetail, Long> {
    @Query("SELECT id FROM InquiryDetail id WHERE id.profile = :profile")
    List<InquiryDetail> findByProfile(Profile profile);
    @Query("SELECT id FROM InquiryDetail id LEFT JOIN FETCH id.inquiry WHERE id.inquiry.inquiryId = :inquiryId")
    Optional<InquiryDetail> findByInquiryId(Long inquiryId);
    @Query("SELECT id FROM InquiryDetail id LEFT JOIN FETCH id.inquiry idi LEFT JOIN FETCH idi.inquiryAnswer")
    List<InquiryDetail> findAllDetailWithAnswer();

    @Query("SELECT id FROM InquiryDetail id LEFT JOIN FETCH id.inquiry idi LEFT JOIN FETCH idi.inquiryAnswer WHERE idi.inquiryCategoryType = :inquiryCategoryType")
    List<InquiryDetail> findAllDetailWithAnswerByInquiryCategoryType(InquiryCategoryType inquiryCategoryType);

//    @Query("SELECT id FROM InquiryDetail id LEFT JOIN FETCH id.inquiry idi WHERE idi.createInquiryDate = :createInquiryDate")
    List<InquiryDetail> findByCreateInquiryDate(LocalDate createInquiryDate);

    @Query("SELECT id FROM InquiryDetail id LEFT JOIN id.inquiry i LEFT JOIN i.inquiryAnswer ia WHERE ia.inquiryAnswerId IS NULL")
    List<InquiryDetail> findWaitingAnswer();

    @Query("SELECT id FROM InquiryDetail id LEFT JOIN id.inquiry i LEFT JOIN i.inquiryAnswer ia WHERE ia.inquiryAnswerId IS NULL AND i.inquiryCategoryType = :inquiryCategoryType")
    List<InquiryDetail> findWaitingAnswerByInquiryCategoryType(InquiryCategoryType inquiryCategoryType);

    @Query("SELECT id FROM InquiryDetail id " +
            "LEFT JOIN FETCH id.inquiry idi " +
            "LEFT JOIN idi.inquiryAnswer ia " +
            "WHERE ia.inquiryAnswerId IS NULL " +
            "ORDER BY createInquiryDate ASC LIMIT 10")
    List<InquiryDetail> findOldestUnansweredInquiries();

    List<InquiryDetail> findByCreateInquiryDateAndInquiry_InquiryCategoryType(LocalDate createInquiryDate, InquiryCategoryType inquiryCategoryType);

    List<InquiryDetail> findByProfile_Account_accountId(Long accountId);
    int countByCreateInquiryDate(LocalDate localDate);
}