package me.muse.CrezyBackend.domain.playlist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private String thumbnailName;

    public Playlist(String playlistName, Account account, String thumbnailName) {
        this.playlistName = playlistName;
        this.account = account;
        this.thumbnailName = thumbnailName;
    }
}
