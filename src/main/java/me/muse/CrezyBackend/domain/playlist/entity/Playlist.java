package me.muse.CrezyBackend.domain.playlist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @Column(name = "playlistId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;
    @Setter
    private String playlistName;
    @Setter
    private String writer; // 추후 Account로 수정
    private List<Integer> likers; // 추후 Account로 수정

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Song> songList = new ArrayList<>();
    @Setter
    private String thumbnailName;

    public Playlist(String playlistName, String writer, String thumbnailName) {
        this.playlistName = playlistName;
        this.writer = writer;
        this.thumbnailName = thumbnailName;
    }
}
