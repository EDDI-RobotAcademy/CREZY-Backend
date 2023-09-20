package me.muse.CrezyBackend.domain.warning.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarningRepository extends JpaRepository<Warning, Long> {
    int countByAccount(Account account);
    @Query("SELECT w FROM Warning w WHERE w.account = :account GROUP BY w.id HAVING COUNT(w) = 1")
    List<Warning> findWarningCountByAccount(Account account);
    List<Warning> findByAccount_AccountId(Long accountId);
}
