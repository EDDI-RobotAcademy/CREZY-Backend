package me.muse.CrezyBackend.config;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.*;
import me.muse.CrezyBackend.domain.account.repository.AccountLoginTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;

@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "profile")
@PropertySource("classpath:admin.properties")
@Getter
@Setter
public class DBInitializer {

    private final AccountRoleTypeRepository roleTypeRepository;
    private final AccountLoginTypeRepository loginTypeRepository;
    private final ProfileRepository profileRepository;
    final private BCryptPasswordEncoder encoder;

    private List<Admin> admins;

    @Getter
    @Setter
    public static class Admin{
        private String email;
        private String password;
        private String nickname;
        private String profileImageName;
    }

    @PostConstruct
    private void init(){
        log.debug("initializer 시작");

        initAccountRoleTypes();
        initAccountLoginTypes();
        initAdminAccounts();

        log.debug("initializer 종료");

    }

    private void initAccountRoleTypes(){
        try{
            final Set<RoleType> roles=
                    roleTypeRepository.findAll().stream()
                            .map(AccountRoleType ::getRoleType)
                            .collect(Collectors.toSet());
            for(RoleType type: RoleType.values()){
                if(!roles.contains(type)){
                    final AccountRoleType role = new AccountRoleType(type);
                    roleTypeRepository.save(role);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void initAccountLoginTypes(){
        try{
            final Set<LoginType> roles=
                    loginTypeRepository.findAll().stream()
                            .map(AccountLoginType ::getLoginType)
                            .collect(Collectors.toSet());
            for(LoginType type: LoginType.values()){
                if(!roles.contains(type)){
                    final AccountLoginType loginType = new AccountLoginType(type);
                    loginTypeRepository.save(loginType);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void initAdminAccounts(){
        try{
            AccountRoleType roleType = roleTypeRepository.findByRoleType(ADMIN).get();
            final List<String> adminEmail = profileRepository.findByAccount_RoleType(roleType)
                    .stream().map(Profile::getEmail).toList();

            for(Admin admin : admins) {
                if (!adminEmail.contains(admin.getEmail())) {
                    profileRepository.save(new Profile(admin.nickname, encoder.encode(admin.password), admin.email, admin.profileImageName, new Account(roleType)));
                }
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}

