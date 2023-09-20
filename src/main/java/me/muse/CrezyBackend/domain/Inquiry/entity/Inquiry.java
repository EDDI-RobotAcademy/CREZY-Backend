package me.muse.CrezyBackend.domain.Inquiry.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.admin.InquiryManage.Entity.InquiryAnswer;

@Entity
@NoArgsConstructor
@Getter
public class Inquiry {
    @Id
    @Column(name = "inquiryId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;
    @OneToOne
    private InquiryCategoryType inquiryCategoryType;
    @OneToOne(cascade = CascadeType.REMOVE)
    @PrimaryKeyJoinColumn(name = "inquiryAnswerId")
    private InquiryAnswer inquiryAnswer;
    public Inquiry(InquiryCategoryType inquiryCategoryType) {
        this.inquiryCategoryType = inquiryCategoryType;
    }
}