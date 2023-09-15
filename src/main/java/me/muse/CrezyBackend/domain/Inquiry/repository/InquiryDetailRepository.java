package me.muse.CrezyBackend.domain.Inquiry.repository;

import me.muse.CrezyBackend.domain.Inquiry.entity.Inquiry;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryDetail;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InquiryDetailRepository extends JpaRepository<InquiryDetail, Long> {
    @Query("SELECT id FROM InquiryDetail id WHERE id.profile = :profile")
    List<InquiryDetail> findByProfile(Profile profile);
    @Query("SELECT id FROM InquiryDetail id LEFT JOIN FETCH id.inquiry WHERE id.inquiry.inquiryId = :inquiryId")
    Optional<InquiryDetail> findByInquiryId(Long inquiryId);
    @Query("SELECT id FROM InquiryDetail id LEFT JOIN FETCH id.inquiry idi LEFT JOIN FETCH idi.inquiryAnswer")
    List<InquiryDetail> findAllDetailWithAnswer();
}