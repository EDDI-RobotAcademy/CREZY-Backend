package me.muse.CrezyBackend.domain.admin.traffic.repository;

import me.muse.CrezyBackend.domain.admin.traffic.entity.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TrafficRepository extends JpaRepository<Traffic, LocalDate> {
    Optional<Traffic> findByDate(LocalDate now);
}
