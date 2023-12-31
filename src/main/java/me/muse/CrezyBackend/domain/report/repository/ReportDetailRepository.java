package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import me.muse.CrezyBackend.domain.report.entity.ReportStatus;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportDetailRepository extends JpaRepository<ReportDetail,Long> {
    @Query("SELECT rd FROM ReportDetail rd JOIN FETCH rd.report ORDER BY rd.reportDetailId DESC")
    List<ReportDetail> findAllWithPage();
    Optional<ReportDetail> findByReport_ReportId(Long reportId);
    List<ReportDetail> findAllByReportedAccountId(Long accountId);

    int countByCreateReportDate(LocalDate localDate);
    @Query("SELECT rd " +
            "FROM ReportDetail rd " +
            "JOIN FETCH rd.report r " +
            "WHERE r.reportStatusType.reportStatus = :statusType " +
            "ORDER BY rd.reportDetailId DESC")
    List<ReportDetail> findByReportStatusType(ReportStatus statusType);

    @Query("SELECT rd " +
            "FROM ReportDetail rd " +
            "JOIN FETCH rd.report r " +
            "WHERE r.reportedCategoryType.reportedCategory = :categoryType " +
            "ORDER BY rd.reportDetailId DESC")
    List<ReportDetail> findByReportedCategoryType(ReportedCategory categoryType);

    @Query("SELECT rd " +
            "FROM ReportDetail rd " +
            "JOIN FETCH rd.report r " +
            "WHERE (r.reportStatusType.reportStatus = :statusType) " +
            "AND (r.reportedCategoryType.reportedCategory = :categoryType) " +
            "ORDER BY rd.reportDetailId DESC")
    List<ReportDetail> findByReportStatusTypeAndReportedCategoryType(ReportStatus statusType, ReportedCategory categoryType);
}

