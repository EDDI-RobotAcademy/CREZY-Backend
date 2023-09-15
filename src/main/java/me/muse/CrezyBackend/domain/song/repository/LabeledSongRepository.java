package me.muse.CrezyBackend.domain.song.repository;

import me.muse.CrezyBackend.domain.song.entity.LabeledSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabeledSongRepository extends JpaRepository<LabeledSong, Long> {
    List<LabeledSong> findByLabel(String label);
}
