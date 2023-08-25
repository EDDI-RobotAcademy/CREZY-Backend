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
    @Column(name = "songId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;
    private String title;
    private String singer;
    private String genre;

    @Setter
    private String link;

    @Lob
    @Setter
    @Column(name="LYRICS", length=4000)
    private String lyrics;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    public Song(String title, String singer, String genre, String link, Playlist playlist) {
        this.title = title;
        this.singer = singer;
        this.genre = genre;
        this.link = link;
        this.playlist = playlist;
    }
}
