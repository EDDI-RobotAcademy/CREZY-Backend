package me.muse.CrezyBackend.domain.Inquiry.repository;

import me.muse.CrezyBackend.domain.Inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    int countByCreateInquiryDate(LocalDate date);
    @Query("SELECT COUNT(i) FROM Inquiry i LEFT JOIN i.inquiryAnswer ia WHERE ia IS NULL")
    int countWaitingAnswer();
}
