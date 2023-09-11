package me.muse.CrezyBackend.domain.likePlaylist.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.likePlaylist.controller.form.PlaylistUsersLikeResponseForm;
import me.muse.CrezyBackend.domain.likePlaylist.entity.LikePlaylist;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikePlaylistServiceImpl implements LikePlaylistService{
    final private PlaylistRepository playlistRepository;
    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private ProfileRepository profileRepository;
    final private LikePlaylistRepository likePlaylistRepository;
    @Override
    @Transactional
    public int likePlaylist(Long playlistId, HttpHeaders headers) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);
        if (maybePlaylist.isEmpty()) {
            return -1;
        }
        Playlist playlist = maybePlaylist.get();

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return playlist.getLikePlaylist().size();
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        Optional<Account> maybeAccount = accountRepository.findById(accountId);

        if(maybeAccount.isEmpty()){
            return playlist.getLikePlaylist().size();
        }

        Account account = maybeAccount.get();

        likePlaylistRepository.save(new LikePlaylist(account, playlist));

        return likePlaylistRepository.countByPlaylist(playlist);
    }

    @Override
    @Transactional
    public boolean isPlaylistLiked(Long playlistId, HttpHeaders headers) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);

        if (maybePlaylist.isEmpty()) {
            return false;
        }

        Playlist playlist = maybePlaylist.get(); // 추천 누른 해당 플레이 리스트 가져옴

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return false;
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        Optional<Account> maybeAccount = accountRepository.findById(accountId);

        if(maybeAccount.isEmpty()){
            return false;
        }

        Account account = maybeAccount.get();

        Set<LikePlaylist> likedPlaylists = account.getLikePlaylist();
        Optional<LikePlaylist> maybeLikePlaylist = likePlaylistRepository.findByAccountAndPlaylist(account, playlist);
        if(maybeLikePlaylist.isEmpty()){
            return false;
        }
        return likedPlaylists.contains(maybeLikePlaylist.get()); // 안에 포함 되어 있으면 true 반환

    }

    @Override
    @Transactional
    public int unlikePlaylist(Long playlistId, HttpHeaders headers) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);

        if (maybePlaylist.isEmpty()) {
            return -1;
        }

        Playlist playlist = maybePlaylist.get();

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return playlist.getLikePlaylist().size();
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        Optional<Account> maybeAccount = accountRepository.findById(accountId);

        if(maybeAccount.isEmpty()){
            return playlist.getLikePlaylist().size();
        }

        Account account = maybeAccount.get();

        LikePlaylist likePlaylist = likePlaylistRepository.findByAccountAndPlaylist(account, playlist)
                .orElseThrow(() -> new IllegalArgumentException("없음"));

        likePlaylistRepository.deleteById(likePlaylist.getLikePlaylistId());

        return likePlaylistRepository.countByPlaylist(playlist);
    }

    @Override
    @Transactional
    public List<PlaylistUsersLikeResponseForm> bringLikePlaylist(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return null;
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        if (accountId == null) {
            return null;
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계정 없음"));

        List<LikePlaylist> likedPlaylists = likePlaylistRepository.findByAccount(account);

        List<PlaylistUsersLikeResponseForm> responseForms = new ArrayList<>();
        for (LikePlaylist likePlaylist : likedPlaylists) {
            Profile profile = profileRepository.findByAccount(likePlaylist.getPlaylist().getAccount())
                    .orElseThrow(() -> new IllegalArgumentException("프로필 없음"));
            PlaylistUsersLikeResponseForm responseForm = new PlaylistUsersLikeResponseForm(
                    likePlaylist.getPlaylist().getPlaylistId(),
                    likePlaylist.getPlaylist().getPlaylistName(),
                    likePlaylist.getPlaylist().getThumbnailName(),
                    profile.getNickname(),
                    likePlaylist.getPlaylist().getSonglist(),
                    likePlaylist.getPlaylist().getLikePlaylist().size()
            );
            responseForms.add(responseForm);
        }
        return responseForms;
    }
}
