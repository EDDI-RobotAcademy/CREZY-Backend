package me.muse.CrezyBackend.domain.playlist.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account")
    List<Playlist> findAll();

    @Query("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.songlist LEFT JOIN FETCH p.account LEFT JOIN FETCH p.likers WHERE p.id = :id")
    Playlist findWithSongById(Long id);

    @Modifying
    @Query("DELETE FROM Playlist p WHERE p.account.accountId = :accountId")
    void deleteByAccountId(Long accountId);

    // JOIN FETCH 부분 추후 수정
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account WHERE p.account.accountId = :accountId ")
    List<Playlist> findByAccountId(Long accountId);
}
