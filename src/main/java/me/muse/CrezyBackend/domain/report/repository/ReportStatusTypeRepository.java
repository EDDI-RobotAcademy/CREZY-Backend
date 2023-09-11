package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.ReportStatus;
import me.muse.CrezyBackend.domain.report.entity.ReportStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportStatusTypeRepository extends JpaRepository<ReportStatusType, Long> {
    Optional<ReportStatusType> findByReportStatus(ReportStatus approve);
}
