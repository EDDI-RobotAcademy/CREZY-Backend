package me.muse.CrezyBackend.domain.Inquiry.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"inquiryDetail"})
@Entity
@NoArgsConstructor
public class InquiryImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String inquiryImagePath;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_detail_id")
    @Setter
    private InquiryDetail inquiryDetail;

    public InquiryImages(String inquiryImagePath) {

        this.inquiryImagePath = inquiryImagePath;
    }
}
