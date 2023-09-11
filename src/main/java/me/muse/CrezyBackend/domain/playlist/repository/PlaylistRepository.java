package me.muse.CrezyBackend.domain.playlist.repository;

import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account JOIN FETCH p.songlist")
    List<Playlist> findAll();

    @Query("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.songlist LEFT JOIN FETCH p.account WHERE p.id = :id")
    Playlist findWithSongById(Long id);

    @Query("SELECT p FROM Playlist p JOIN FETCH p.account LEFT JOIN FETCH p.songlist " +
            "WHERE p.account.accountId = :accountId ")
    List<Playlist> findByAccountId(Long accountId);
}
