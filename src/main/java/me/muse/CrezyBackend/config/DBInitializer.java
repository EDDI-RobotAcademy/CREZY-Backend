package me.muse.CrezyBackend.config;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategory;
import me.muse.CrezyBackend.domain.Inquiry.entity.InquiryCategoryType;
import me.muse.CrezyBackend.domain.Inquiry.repository.InquiryCategoryTypeRepository;
import me.muse.CrezyBackend.domain.account.entity.*;
import me.muse.CrezyBackend.domain.account.repository.AccountLoginTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.AccountRoleTypeRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.report.entity.ReportStatus;
import me.muse.CrezyBackend.domain.report.entity.ReportStatusType;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategory;
import me.muse.CrezyBackend.domain.report.entity.ReportedCategoryType;
import me.muse.CrezyBackend.domain.report.repository.ReportStatusTypeRepository;
import me.muse.CrezyBackend.domain.report.repository.ReportedCategoryTypeRepository;
import me.muse.CrezyBackend.domain.song.entity.LabeledSong;
import me.muse.CrezyBackend.domain.song.repository.LabeledSongRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
@PropertySource("classpath:youtube.properties")
public class DBInitializer {

    private final AccountRoleTypeRepository roleTypeRepository;
    private final AccountLoginTypeRepository loginTypeRepository;
    private final ProfileRepository profileRepository;
    final private BCryptPasswordEncoder encoder;
    final private ReportStatusTypeRepository reportStatusTypeRepository;
    final private ReportedCategoryTypeRepository reportedCategoryType;
    final private InquiryCategoryTypeRepository inquiryCategoryTypeRepository;
    final private LabeledSongRepository labeledSongRepository;

    @Value("${youtube.lyricsAddress}")
    private String lyricsAddress;

    private List<Admin> admins;

    @Getter
    @Setter
    public static class Admin{
        private String email;
        private String password;
        private String nickname;
    }

    @PostConstruct
    private void init(){
        log.debug("initializer 시작");

        initAccountRoleTypes();
        initAccountLoginTypes();
        initAdminAccounts();
        initReportStatusType();
        initReportedCategoryType();
        initInquiryCategoryType();
        initLabeledSong();

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
                    profileRepository.save(new Profile(admin.nickname, encoder.encode(admin.password), admin.email, null, new Account(roleType)));
                }
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void initReportStatusType(){
        try{
            final Set<ReportStatus> reportStatusSet=
                    reportStatusTypeRepository.findAll().stream()
                            .map(ReportStatusType::getReportStatus)
                            .collect(Collectors.toSet());
            for(ReportStatus status: ReportStatus.values()){
                if(!reportStatusSet.contains(status)){
                    final ReportStatusType statusType = new ReportStatusType(status);
                    reportStatusTypeRepository.save(statusType);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void initReportedCategoryType(){
        try{
            final Set<ReportedCategory> reportedCategorySetSet=
                    reportedCategoryType.findAll().stream()
                            .map(ReportedCategoryType::getReportedCategory)
                            .collect(Collectors.toSet());
            for(ReportedCategory category: ReportedCategory.values()){
                if(!reportedCategorySetSet.contains(category)){
                    final ReportedCategoryType statusType = new ReportedCategoryType(category);
                    reportedCategoryType.save(statusType);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void initInquiryCategoryType(){
        try{
            final Set<InquiryCategory> inquiryCategorySet=
                    inquiryCategoryTypeRepository.findAll().stream()
                            .map(InquiryCategoryType::getInquiryCategory)
                            .collect(Collectors.toSet());
            for(InquiryCategory category: InquiryCategory.values()){
                if(!inquiryCategorySet.contains(category)){
                    final InquiryCategoryType categoryType = new InquiryCategoryType(category);
                    inquiryCategoryTypeRepository.save(categoryType);
                }
            }
        }
        catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void initLabeledSong(){
        String url = "http://" + lyricsAddress + "/excel-data";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<LabeledSong>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<LabeledSong>>() {});

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            List<LabeledSong> labeledSongList = labeledSongRepository.findAll();

            List<LabeledSong> labeledSongs = responseEntity.getBody();
            for(LabeledSong labeledSong : labeledSongs){
                if(!labeledSongList.contains(labeledSong)){
                    labeledSongRepository.save(labeledSong);
                }
            }
        }
    }
}

