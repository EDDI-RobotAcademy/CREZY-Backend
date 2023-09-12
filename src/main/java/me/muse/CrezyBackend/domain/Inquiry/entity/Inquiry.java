package me.muse.CrezyBackend.domain.Inquiry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Inquiry {
    @Id
    @Column(name = "inquiryId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;
    @CreationTimestamp
    private LocalDate createInquiryDate;
    @OneToOne
    private InquiryCategoryType inquiryCategoryType;
    public Inquiry(InquiryCategoryType inquiryCategoryType) {
        this.inquiryCategoryType = inquiryCategoryType;
    }
}