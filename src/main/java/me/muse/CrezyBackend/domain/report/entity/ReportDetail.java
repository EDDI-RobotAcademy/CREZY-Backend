package me.muse.CrezyBackend.domain.report.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Account;
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
    @OneToOne
    private Account reporter;
    @OneToOne
    private Account reportedAccount;
    private String reportContent;
    @CreationTimestamp
    private LocalDate createReportDate;
}
