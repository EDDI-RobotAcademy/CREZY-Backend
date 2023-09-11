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
    private String reportContent;
    @OneToOne
    private Report report;
    @CreationTimestamp
    private LocalDate createReportDate;
}
