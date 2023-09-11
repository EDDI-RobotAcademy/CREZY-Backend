package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.ReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDetailRepository extends JpaRepository<ReportDetail, Long> {
}
