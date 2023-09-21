package me.muse.CrezyBackend.domain.admin.InquiryManage.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.Inquiry.entity.Inquiry;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
@Entity
@NoArgsConstructor
@Getter
public class InquiryAnswer {
    @Id
    @Column(name = "inquiryAnswerId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryAnswerId;
    @Setter
    private String inquiryAnswer;
    @CreationTimestamp
    private LocalDate createInquiryAnswerDate;
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "inquiryId")
    private Inquiry inquiry;

    public InquiryAnswer(String inquiryAnswer, Inquiry inquiry) {
        this.inquiryAnswer = inquiryAnswer;
        this.inquiry = inquiry;
    }
}
