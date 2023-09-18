package me.muse.CrezyBackend.domain.song.repository;

import me.muse.CrezyBackend.domain.song.entity.SongStatusType;
import me.muse.CrezyBackend.domain.song.entity.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongStatusRepository extends JpaRepository<SongStatusType, Long> {

    Optional<SongStatusType> findByStatusType(StatusType statusType);
}
