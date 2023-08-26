package me.muse.CrezyBackend.domain.song.repository;

import me.muse.CrezyBackend.domain.song.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Modifying
    @Query("delete from Song s where s.songId in :songIds")
    void deleteAllByIds(List<Long> songIds);
}
