package me.muse.CrezyBackend.domain.account.repository;

import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRoleTypeRepository extends JpaRepository<AccountRoleType, Long> {

    Optional<AccountRoleType> findByRoleType(RoleType roleType);
}
