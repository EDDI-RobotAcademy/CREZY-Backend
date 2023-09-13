package me.muse.CrezyBackend.domain.likePlaylist.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.likePlaylist.entity.LikePlaylist;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikePlaylistRepository extends JpaRepository<LikePlaylist, Long> {
    Optional<LikePlaylist> findByAccountAndPlaylist(Account account, Playlist playlist);
    List<LikePlaylist> findByPlaylist(Playlist playlist);
    int countByPlaylist(Playlist playlist);
    @Query("SELECT l FROM LikePlaylist l LEFT JOIN FETCH l.playlist p LEFT JOIN FETCH p.songlist WHERE l.account = :account")
    List<LikePlaylist> findByAccount(Account account);
    @Modifying
    void deleteById(Long likePlaylistId);
    Integer countByAccount(Account account);
}
