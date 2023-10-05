package me.muse.CrezyBackend.domain.playlist.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account JOIN FETCH p.songlist")
    List<Playlist> findAll();
    @Query("SELECT p FROM Playlist p JOIN FETCH p.account LEFT JOIN FETCH p.songlist")
    List<Playlist> findAllPlaylist();

    @Query("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.songlist LEFT JOIN FETCH p.account WHERE p.id = :id")
    Playlist findWithSongById(Long id);

    @Query("SELECT p FROM Playlist p JOIN FETCH p.account LEFT JOIN FETCH p.songlist " +
            "WHERE p.account.accountId = :accountId ")
    List<Playlist> findByAccountId(Long accountId);

    Integer countByAccount(Account isAccount);
    List<Playlist> findPlaylistIdByAccount(Account account);
    @Query("SELECT p FROM Playlist p WHERE p.account = :account AND p.createDate = :createDate")
    List<Playlist> countByAccountAndCreateDate(Account account, LocalDate createDate);
    List<Playlist> findByCreateDate(LocalDate localDate);
    @Query("SELECT p FROM Playlist p LEFT JOIN FETCH p.songlist ORDER BY p.createDate DESC")
    List<Playlist> findAllWithPage();
    @Query("SELECT p FROM Playlist p ORDER BY SIZE(p.likePlaylist) DESC")
    List<Playlist> findAllSortByLikePlaylist();

    @Query("SELECT p FROM Playlist p WHERE p.songlist IS EMPTY ORDER BY p.createDate DESC")
    List<Playlist> findAllBySongEmpty();
    List<Playlist> findPlaylistByAccount_AccountId(Long accountId);

    @Query("SELECT p " +
            "FROM Playlist p " +
            "JOIN FETCH p.songlist " +
            "WHERE (p.account.accountId IN " +
            "(SELECT pf.account.accountId FROM Profile pf WHERE pf.nickname LIKE %:keyword%) " +
            "OR p.playlistName LIKE %:keyword%) " +
            "ORDER BY p.createDate DESC")
    List<Playlist> findByPlaylistNameAndNickname(String keyword);
    @Query("SELECT p " +
            "FROM Playlist p " +
            "WHERE (p.account.accountId IN " +
            "(SELECT pf.account.accountId FROM Profile pf WHERE pf.nickname LIKE %:keyword%) " +
            "OR p.playlistName LIKE %:keyword%) " +
            "ORDER BY p.createDate DESC")
    List<Playlist> findAllByPlaylistNameAndNickname(String keyword);
    @Query("SELECT p FROM Playlist p JOIN FETCH p.songlist ORDER BY p.playlistId DESC")
    List<Playlist> findAllWithPageForService();

    @Query("SELECT p FROM Playlist p JOIN FETCH p.songlist ORDER BY SIZE(p.likePlaylist) DESC")
    List<Playlist> findAllSortByLikePlaylistForService();

}
