package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.report.entity.ReportedCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedCategoryTypeRepository extends JpaRepository<ReportedCategoryType, Long> {
}
