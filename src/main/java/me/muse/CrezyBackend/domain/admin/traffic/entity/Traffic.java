package me.muse.CrezyBackend.domain.admin.traffic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Traffic {

    @Id
    private LocalDate date;
    @Setter
    @ColumnDefault("0")
    private int loginCount;
    @Setter
    @ColumnDefault("0")
    private int analysisCount;

    public Traffic(LocalDate date) {
        this.date = date;
    }
}
