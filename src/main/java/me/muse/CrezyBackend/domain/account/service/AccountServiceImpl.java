package me.muse.CrezyBackend.domain.account.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.controller.form.AccountInfoResponseForm;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private PlaylistRepository playlistRepository;

    @Override
    public void logout(String userToken) {
        redisService.deleteByKey(userToken);
    }
    @Override
    public Boolean checkNickname(String nickname) {
        final Optional<Account> maybeAccount = accountRepository.findByNickname(nickname);
        if (maybeAccount.isPresent()) {
            return false;
        }
        return true;
    }
    @Override
    public String changeNickname(String userToken, String nickname) {
        Long accountId = redisService.getValueByKey(userToken);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setNickname(nickname);
        accountRepository.save(account);
        return account.getNickname();
    }

    @Override
    @Transactional
    public Boolean withdrawal(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(accountId);

        if (maybeAccount.isPresent()) {
            Account account = maybeAccount.get();

            for (Playlist playlist : account.getLikedPlaylists()) {
                playlist.removeFromLikers(account);
                account.removeFromLikedPlaylists(playlist);
            }

            playlistRepository.deleteByAccountId(accountId);
            accountRepository.deleteById(accountId);
            redisService.deleteByKey(authValues.get(0));

            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public AccountInfoResponseForm returnAccountInfo(HttpHeaders headers) {
        final List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }

        final Long accountId = redisService.getValueByKey(authValues.get(0));
        final Optional<Account> maybeAccount = accountRepository.findById(accountId);

        if (maybeAccount.isEmpty()) {
            return null;
        }

        final Account account = maybeAccount.get();

        final AccountInfoResponseForm responseForm = new AccountInfoResponseForm(
               account.getAccountId(), account.getEmail(), account.getNickname(), account.getPlaylist().size(),
                account.getLikedPlaylists().size(), account.getProfileImageName()
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
        Optional<Account> maybeAccount = accountRepository.findById(accountId);

        if (maybeAccount.isPresent()) {
            Account account = maybeAccount.get();
            account.setProfileImageName(profileImageName);
            accountRepository.save(account);
            return account.getProfileImageName();
        }

        return null;
    }
}