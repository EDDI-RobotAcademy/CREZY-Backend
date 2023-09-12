package me.muse.CrezyBackend.domain.Inquiry.repository;

import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategory;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InquiryCategoryTypeRepository extends JpaRepository<InquiryCategoryType, Long> {

    @Query("SELECT i FROM InquiryCategoryType i WHERE i.InquiryCategory = :inquiryCategory")
    Optional<InquiryCategoryType> findByInquiryCategory(@Param("inquiryCategory") InquiryCategory inquiryCategoryType);
}
