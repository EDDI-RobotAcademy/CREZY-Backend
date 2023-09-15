package me.muse.CrezyBackend.domain.account.repository;

import me.muse.CrezyBackend.domain.account.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByEmailAndAccount_LoginType(String email, AccountLoginType loginType);
    Optional<Profile> findByNickname(String nickname);
    Optional<Profile> findByAccount(Account account);
    List<Profile> findByAccount_RoleType(AccountRoleType roleType);
    @Query("SELECT p FROM Profile p JOIN FETCH p.account a WHERE a.roleType = :roleType")
    List<Profile> findByAccount_RoleTypeWithPage(Pageable pageable, AccountRoleType roleType);
    @Query("SELECT p FROM Profile p JOIN FETCH p.account a WHERE a.roleType = :roleType")
    List<Profile> findAllBlacklistWithPage(Pageable pageable, AccountRoleType roleType);
    @Query("SELECT p FROM Profile p JOIN FETCH p.account WHERE p.account.accountId = :reportedAccountId")
    Optional<Profile> findByAccount_AccountId(Long reportedAccountId);
    @Query("SELECT p FROM Profile p JOIN FETCH p.account WHERE p.account.accountId = :reportedAccountId AND p.account.roleType = :roleType")
    Optional<Profile> findByAccount_AccountIdAndRoleType(Long reportedAccountId, AccountRoleType roleType);
}