package me.muse.CrezyBackend.domain.account.repository;

import me.muse.CrezyBackend.domain.account.entity.AccountLoginType;
import me.muse.CrezyBackend.domain.account.entity.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLoginTypeRepository extends JpaRepository<AccountLoginType,Long> {

    Optional<AccountLoginType> findByLoginType(LoginType loginType);
}
