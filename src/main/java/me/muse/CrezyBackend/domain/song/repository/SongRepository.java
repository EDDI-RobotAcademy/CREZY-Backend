package me.muse.CrezyBackend.domain.song.repository;

import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.entity.SongStatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Modifying
    @Query("delete from Song s where s.songId in :songIds")
    void deleteAllByIds(List<Long> songIds);
    Integer countByPlaylist(Playlist playlist);
    @Query("SELECT COUNT(p) FROM Song p WHERE p.playlist = :playlist AND p.createDate = :createDate")
    Integer countByPlaylistAndCreateDate(Playlist playlist, LocalDate createDate);
    List<Song> findByPlaylist_PlaylistId(Long playlistId);
    List<Song> findByCreateDate(LocalDate localDate);
    List<Song> findByStatusTypeOrderByTitleAsc(SongStatusType songStatusType);
    List<Song> findByStatusTypeOrderByTitleDesc(SongStatusType songStatusType);
    @Query("SELECT s FROM Song s WHERE s.title LIKE %:keyword% OR s.singer LIKE %:keyword% ORDER BY s.title")
    List<Song> findAllByTitleAndSingerOrderByAsc(String keyword);
    @Query("SELECT s FROM Song s WHERE s.title LIKE %:keyword% OR s.singer LIKE %:keyword% ORDER BY s.title DESC")
    List<Song> findAllByTitleAndSingerOrderByDesc(String keyword);
    List<Song> findByPlaylist_PlaylistIdOrderBySongIndexAsc(Long playlistId);

    @Query("SELECT MAX(s.songIndex) FROM Song s WHERE s.playlist = :playlist")
    Long findMaxSongIndexByPlaylist(Playlist playlist);
}
