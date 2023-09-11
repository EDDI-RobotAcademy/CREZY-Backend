package me.muse.CrezyBackend.domain.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="reportStatus")
@NoArgsConstructor
public class ReportStatusType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReportStatusId;

    @Enumerated(EnumType.STRING)
    @Getter
    private ReportStatus reportStatus;

}
