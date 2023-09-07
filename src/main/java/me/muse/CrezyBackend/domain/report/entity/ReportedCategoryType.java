package me.muse.CrezyBackend.domain.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.LoginType;
@Entity
@Table(name="reportedCategory")
@NoArgsConstructor
public class ReportedCategoryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportedCategoryId;

    @Enumerated(EnumType.STRING)
    @Getter
    private ReportedCategory reportedCategory;

    public ReportedCategoryType(ReportedCategory reportedCategory) {
        this.reportedCategory = reportedCategory;
    }
}
