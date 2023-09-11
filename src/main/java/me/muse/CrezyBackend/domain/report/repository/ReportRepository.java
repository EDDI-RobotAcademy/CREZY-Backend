package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {
    List<Report> findByReportedCategoryType(ReportedCategory reportedCategory);
}




