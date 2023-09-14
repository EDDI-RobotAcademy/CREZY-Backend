package me.muse.CrezyBackend.domain.song.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LabeledSong {
    @Id
    @Column(name = "labeledSongId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labeledSongId;
    private String title;
    private String artist;

    @Lob
    @Setter
    @Column(name="LYRICS", length=4000)
    private String lyrics;

    private String label;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabeledSong that = (LabeledSong) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(artist, that.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }
}