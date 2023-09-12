package me.muse.CrezyBackend.domain.Inquiry.repository;

import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryDetailRepository extends JpaRepository<InquiryDetail, Long> {
}
