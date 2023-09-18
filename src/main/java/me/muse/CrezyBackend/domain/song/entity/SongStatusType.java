package me.muse.CrezyBackend.domain.song.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "statusType")
@NoArgsConstructor
public class SongStatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Getter
    private StatusType statusType;


    public SongStatusType(StatusType statusType) {
        this.statusType = statusType;
    }
}
