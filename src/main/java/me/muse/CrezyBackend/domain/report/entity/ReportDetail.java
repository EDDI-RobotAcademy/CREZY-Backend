package me.muse.CrezyBackend.domain.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class ReportDetail {
    @Id
    @Column(name = "reportDetailId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportDetailId;
    private Long reporterAccountId;
    private Long reportedAccountId;
    private Long reportedId;
    private String reportContent;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Report report;
    @CreationTimestamp
    private LocalDate createReportDate;

    public ReportDetail(Long reporterAccountId, Long reportedAccountId, Long reportedId, String reportContent, Report report) {
        this.reporterAccountId = reporterAccountId;
        this.reportedAccountId = reportedAccountId;
        this.reportedId = reportedId;
        this.reportContent = reportContent;
        this.report = report;
    }

    public ReportDetail(Long reporterAccountId, Long reportedAccountId, String reportContent, Report report) {
        this.reporterAccountId = reporterAccountId;
        this.reportedAccountId = reportedAccountId;
        this.reportContent = reportContent;
        this.report = report;
    }
}
