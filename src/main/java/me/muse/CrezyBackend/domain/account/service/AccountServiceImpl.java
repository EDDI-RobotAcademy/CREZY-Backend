package me.muse.CrezyBackend.domain.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.controller.form.AccountInfoResponseForm;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.likePlaylist.entity.LikePlaylist;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private ProfileRepository profileRepository;
    final private LikePlaylistRepository likePlaylistRepository;


    @Override
    public void logout(String userToken) {
        redisService.deleteByKey(userToken);
    }
    @Override
    public Boolean checkNickname(String nickname) {
        Optional<Profile> maybeProfile = profileRepository.findByNickname(nickname);
        if (maybeProfile.isPresent()) {
            return false;
        }
        return true;
    }
    @Override
    public String changeNickname(String userToken, String nickname) {
        Long accountId = redisService.getValueByKey(userToken);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profile.setNickname(nickname);
        profileRepository.save(profile);
        return profile.getNickname();
    }

    @Override
    @Transactional
    public Boolean withdrawal(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        for(LikePlaylist likePlaylist : account.getLikePlaylist()){
            likePlaylistRepository.deleteById(likePlaylist.getLikePlaylistId());
        }

        for(Playlist playlist : account.getPlaylist()){
            for(LikePlaylist likePlaylist : playlist.getLikePlaylist()){
                likePlaylistRepository.deleteById(likePlaylist.getLikePlaylistId());
            }
        }

        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profileRepository.deleteById(profile.getProfileId());
        redisService.deleteByKey(authValues.get(0));

        return true;
    }

    @Override
    @Transactional
    public AccountInfoResponseForm returnAccountInfo(HttpHeaders headers) {
        final List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }

        final Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));;
        final AccountInfoResponseForm responseForm = new AccountInfoResponseForm(
               account.getAccountId(), profile.getEmail(), profile.getNickname(), account.getPlaylist().size(),
                account.getLikePlaylist().size(), profile.getProfileImageName()
        );

        return responseForm;
    }

    @Override
    public String changeProfileImage(HttpHeaders headers, String profileImageName) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profile.setProfileImageName(profileImageName);
        profileRepository.save(profile);
        return profile.getProfileImageName();

    }
}