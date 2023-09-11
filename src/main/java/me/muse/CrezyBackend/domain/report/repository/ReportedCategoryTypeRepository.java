package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.ReportedCategory;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportedCategoryTypeRepository extends JpaRepository<ReportedCategoryType, Long> {
    Optional<ReportedCategoryType> findByReportedCategory(ReportedCategory categoryType);
}
