package me.muse.CrezyBackend.domain.account.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    @Query("SELECT COUNT(a) FROM Account a WHERE a.createDate = :date AND a.roleType = :roleType")
    Integer findByCreateDateAndAccountRoleType(LocalDate date, AccountRoleType roleType);
    @Query("SELECT COUNT(a) FROM Account a WHERE a.roleType = :roleType")
    Integer findByAccountRoleType(AccountRoleType roleType);

    Optional<Account> findByPlaylist_playlistId(Long reportedPlaylistId);
}