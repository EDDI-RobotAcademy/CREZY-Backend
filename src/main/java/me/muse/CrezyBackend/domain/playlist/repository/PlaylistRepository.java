package me.muse.CrezyBackend.domain.playlist.repository;

import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
