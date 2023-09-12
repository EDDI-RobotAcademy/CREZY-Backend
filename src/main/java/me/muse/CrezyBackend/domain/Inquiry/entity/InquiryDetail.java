package me.muse.CrezyBackend.domain.Inquiry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.report.entity.Report;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class InquiryDetail {
    @Id
    @Column(name = "inquiryDetailId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryDetailId;
    private String inquiryTitle;
    private String inquiryContent;
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "inquiryDetail", fetch = FetchType.LAZY)
    private List<InquiryImages> inquiryImageNames = new ArrayList<>();
    @ManyToOne
    private Profile profile;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Inquiry inquiry;

    public InquiryDetail(String inquiryTitle, String inquiryContent, Profile profile, Inquiry inquiry) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.profile = profile;
        this.inquiry = inquiry;
    }

    public void setInquiryImages(InquiryImages inquiryImages) {
        inquiryImages.setInquiryDetail(this);
        inquiryImageNames.add(inquiryImages);
    }
}
