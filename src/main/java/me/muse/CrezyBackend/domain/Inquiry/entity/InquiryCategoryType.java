package me.muse.CrezyBackend.domain.Inquiry.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="inquiryCategory")
@NoArgsConstructor
public class InquiryCategoryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryCategoryId;

    @Enumerated(EnumType.STRING)
    @Getter
    private InquiryCategory InquiryCategory;

    public InquiryCategoryType(InquiryCategory InquiryCategory) {

        this.InquiryCategory = InquiryCategory;
    }
}
