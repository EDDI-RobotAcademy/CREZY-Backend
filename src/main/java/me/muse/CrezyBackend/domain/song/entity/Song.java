package me.muse.CrezyBackend.domain.song.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String singer;
    private String genre;
    private String link;

    @Lob
    @Setter
    @Column(name="LYRICS", length=4000)
    private String lyrics;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;
}
