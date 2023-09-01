package me.muse.CrezyBackend.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;

import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class Account {
    @Id
    @Column(name = "accountId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Setter
    private String nickname;
    private String password;
    private String email;
    @Setter
    private String profileImageName;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Playlist> playlist = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "playlist_likes",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> likedPlaylists = new HashSet<>();

    public Account(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Account(String nickname, String email, String profileImageName) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageName = profileImageName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public void removeFromLikedPlaylists(Playlist playlist) {

        likedPlaylists.remove(playlist);
    }

}
