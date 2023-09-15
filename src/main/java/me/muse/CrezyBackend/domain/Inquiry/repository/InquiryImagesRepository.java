package me.muse.CrezyBackend.domain.Inquiry.repository;

import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryImagesRepository extends JpaRepository<InquiryImages, Long> {
    @Query("SELECT ii FROM InquiryImages ii JOIN FETCH ii.inquiryDetail WHERE ii.inquiryDetail.inquiryDetailId = :inquiryDetailId")
    List<InquiryImages> findByInquiryDetailId(Long inquiryDetailId);

    @Modifying
    @Query("DELETE FROM InquiryImages ii WHERE ii.inquiryDetail.inquiryDetailId = :inquiryDetailId")
    void deleteAllByInquiryDetailId(@Param("inquiryDetailId") Long inquiryDetailId);

}
