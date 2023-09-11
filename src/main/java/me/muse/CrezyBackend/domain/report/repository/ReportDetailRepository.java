package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ReportDetailRepository extends JpaRepository<ReportDetail,Long> {
    @Query("SELECT rd FROM ReportDetail rd JOIN FETCH rd.report")
    List<ReportDetail> findAllWithPage(Pageable pageable);
    @Query("SELECT rd FROM ReportDetail rd LEFT JOIN FETCH rd.report WHERE rd.report.reportId = :reportId")
    Optional<ReportDetail> findByReportId(Long reportId);
}

