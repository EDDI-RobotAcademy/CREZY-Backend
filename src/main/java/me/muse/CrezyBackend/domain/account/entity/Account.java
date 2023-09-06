package me.muse.CrezyBackend.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class Account {
    @Id
    @Column(name = "accountId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @CreationTimestamp
    private LocalDate createDate;
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Playlist> playlist = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "playlist_likes",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private Set<Playlist> likedPlaylists = new HashSet<>();
    @OneToOne
    private AccountLoginType loginType;
    @OneToOne
    private AccountRoleType roleType;

    public Account(AccountLoginType loginType, AccountRoleType roleType) {
        this.loginType = loginType;
        this.roleType = roleType;
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
