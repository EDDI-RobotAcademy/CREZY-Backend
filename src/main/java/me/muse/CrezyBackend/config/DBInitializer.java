package me.muse.CrezyBackend.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.AccountLoginType;
import me.muse.CrezyBackend.domain.account.entity.AccountRoleType;
import me.muse.CrezyBackend.domain.account.entity.LoginType;
import me.muse.CrezyBackend.domain.account.entity.RoleType;
import me.muse.CrezyBackend.domain.account.repository.AccountLoginTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBInitializer {

    private final AccountRoleTypeRepository roleTypeRepository;
    private final AccountLoginTypeRepository loginTypeRepository;

    @PostConstruct
    private void init(){
        log.debug("initializer 시작");

        initAccountRoleTypes();
        initAccountLoginTypes();

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
                    final AccountLoginType role = new AccountLoginType(type);
                    loginTypeRepository.save(role);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
