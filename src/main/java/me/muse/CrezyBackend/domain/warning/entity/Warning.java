package me.muse.CrezyBackend.domain.warning.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.report.entity.Report;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Warning {
    @Id
    @Column(name = "warningId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warningId;
    @OneToOne
    private Account account;
    @OneToOne
    private Report report;
    @CreationTimestamp
    private LocalDate createWarningDate;

    public Warning(Account account, Report report) {
        this.account = account;
        this.report = report;
    }
}
