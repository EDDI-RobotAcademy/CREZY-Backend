package me.muse.CrezyBackend.domain.likePlaylist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;

@Entity
@NoArgsConstructor
@Getter
public class LikePlaylist {
    @Id
    @Column(name = "likePlaylistId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likePlaylistId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    public LikePlaylist(Account account, Playlist playlist) {
        this.account = account;
        this.playlist = playlist;
    }
}
