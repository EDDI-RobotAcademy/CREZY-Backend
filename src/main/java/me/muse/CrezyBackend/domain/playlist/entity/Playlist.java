package me.muse.CrezyBackend.domain.playlist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.account.entity.Account;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany(mappedBy = "likedPlaylists", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Account> likers;

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Song> songList = new ArrayList<>();
    @Setter
    private String thumbnailName;

    public Playlist(String playlistName, String thumbnailName, Account account) {
        this.playlistName = playlistName;
        this.thumbnailName = thumbnailName;
        this.account = account;
    }
}
