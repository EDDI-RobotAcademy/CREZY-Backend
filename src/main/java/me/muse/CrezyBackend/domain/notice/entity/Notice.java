package me.muse.CrezyBackend.domain.notice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Notice {
    @Id
    @Column(name = "noticeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    @CreationTimestamp
    private LocalDate createNoticeDate;
    private Long accountId;
}
