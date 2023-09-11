package me.muse.CrezyBackend.domain.account.repository;

import me.muse.CrezyBackend.domain.account.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByEmailAndAccount_LoginType(String email, AccountLoginType loginType);
    Optional<Profile> findByNickname(String nickname);
    Optional<Profile> findByAccount(Account account);
    List<Profile> findByAccount_RoleType(AccountRoleType roleType);
}