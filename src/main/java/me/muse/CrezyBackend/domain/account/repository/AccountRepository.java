package me.muse.CrezyBackend.domain.account.repository;

import me.muse.CrezyBackend.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByEmail(String email);
}