package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ReportDetailRepository extends JpaRepository<ReportDetail,Long> {
    @Query("SELECT rd FROM ReportDetail rd JOIN FETCH rd.report")
    List<ReportDetail> findAllWithPage(Pageable pageable);
    Optional<ReportDetail> findByReport_ReportId(Long reportId);
    List<ReportDetail> findAllByReportedAccountId(Long accountId);

}

