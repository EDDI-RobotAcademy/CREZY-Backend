package me.muse.CrezyBackend.domain.report.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.report.entity.Report;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {
    List<Report> findByReportedCategoryType(ReportedCategory reportedCategory);
    @Query("SELECT COUNT(r) FROM Report r WHERE r.reportedCategoryType.reportedCategory = :reportedCategory")
    int countByReportedCategoryType(ReportedCategory reportedCategory);
    Integer countByReportId(Long reportId);
}




