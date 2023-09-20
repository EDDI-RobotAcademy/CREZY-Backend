package me.muse.CrezyBackend.domain.song.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @Column(name = "songId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    @Setter
    private String title;

    @Setter
    private String singer;

    @Setter
    private String link;

    @Lob
    @Setter
    @Column(name="LYRICS", length=4000)
    private String lyrics;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;
    @CreationTimestamp
    private LocalDate createDate;
    @Setter
    private String blockedDate;

    @OneToOne
    @Setter
    private SongStatusType statusType;

    public Song(String title, String singer, String link, String lyrics, Playlist playlist) {
        this.title = title;
        this.singer = singer;
        this.link = link;
        this.lyrics = lyrics;
        this.playlist = playlist;
    }
}
