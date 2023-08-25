package me.muse.CrezyBackend.domain.song.repository;

import me.muse.CrezyBackend.domain.song.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
