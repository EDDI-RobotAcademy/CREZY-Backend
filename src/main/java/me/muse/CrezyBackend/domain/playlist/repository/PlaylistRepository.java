package me.muse.CrezyBackend.domain.playlist.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account JOIN FETCH p.songList LEFT JOIN FETCH p.likers")
    List<Playlist> findAll();
}
