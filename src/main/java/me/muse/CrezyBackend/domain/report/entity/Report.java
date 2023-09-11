package me.muse.CrezyBackend.domain.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Report {
    @Id
    @Column(name = "reportId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    @OneToOne
    private ReportedCategoryType reportedCategoryType;
    @OneToOne
    @Setter
    private ReportStatusType reportStatusType;
    @UpdateTimestamp
    private LocalDate updateReportDate;
}
