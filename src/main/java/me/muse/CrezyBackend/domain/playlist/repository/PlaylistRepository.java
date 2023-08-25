package me.muse.CrezyBackend.domain.playlist.repository;

import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account JOIN FETCH p.songlist LEFT JOIN FETCH p.likers")
    List<Playlist> findAll();

    @Query("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.songlist LEFT JOIN FETCH p.account LEFT JOIN FETCH p.likers WHERE p.id = :id")
    Playlist findWithSongById(Long id);

}
