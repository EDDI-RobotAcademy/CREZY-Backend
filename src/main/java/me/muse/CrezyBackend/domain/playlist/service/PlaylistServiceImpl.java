package me.muse.CrezyBackend.domain.playlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.likePlaylist.entity.LikePlaylist;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.controller.form.*;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.entity.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService{

    final private PlaylistRepository playlistRepository;
    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private ProfileRepository profileRepository;
    final private LikePlaylistRepository likePlaylistRepository;
    @Override
    @Transactional
    public Page<PlaylistResponseForm> list(Integer page) {
        List<Playlist> playlists = playlistRepository.findAll();
        Pageable pageable = PageRequest.of(page - 1, 10);
        List<PlaylistResponseForm> responseForms = new ArrayList<>();
        for (Playlist playlist : playlists) {
            String thumbnailName = playlist.getThumbnailName();
            int likeCount = playlist.getLikePlaylist() != null ? playlist.getLikePlaylist().size() : 0;
            int songCount = playlist.getSonglist() != null ? playlist.getSonglist().size() : 0;

            // 썸네일을 등록하지 않았다면 유튜브 링크의 썸네일을 가져오도록
            if (thumbnailName == null && !playlist.getSonglist().isEmpty()) {
                thumbnailName = playlist.getSonglist().get(0).getLink();
            }
            Profile profile = profileRepository.findByAccount(playlist.getAccount())
                    .orElseThrow(() -> new IllegalArgumentException("프로필 없음"));
            PlaylistResponseForm responseForm = new PlaylistResponseForm(
                    playlist.getPlaylistId(), playlist.getPlaylistName(), profile.getNickname(),
                    likeCount, songCount, thumbnailName);

            responseForms.add(responseForm);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseForms.size());

        return new PageImpl<>(
                responseForms.subList(start, end),
                pageable,
                responseForms.size());
    }

    @Override
    @Transactional
    public PlaylistReadResponseForm readPlayList(Long playlistId) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);
        if (maybePlaylist.isPresent()) {
            Playlist playlist = maybePlaylist.get();

            List<Song> resultList = playlist.getSonglist();

            List<Song> openSongs = resultList.stream()
                    .filter(song -> song.getStatusType() == null || song.getStatusType().getStatusType() != StatusType.BLOCK)
                    .distinct()
                    .collect(Collectors.toList());

            Profile profile = profileRepository.findByAccount(playlist.getAccount())
                    .orElseThrow(() -> new IllegalArgumentException("프로필 없음"));

            List<LikePlaylist> likePlaylists = likePlaylistRepository.findByPlaylist(playlist);

            return new PlaylistReadResponseForm(playlist.getPlaylistName(),
                    profile.getNickname(),
                    playlist.getThumbnailName(),
                    likePlaylists.size(),
                    openSongs);
        }
        return null;
    }

    @Override
    @Transactional
    public PlaylistReadResponseForm readMyPagePlaylist(Long playlistId) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);
        if (maybePlaylist.isPresent()) {
            Playlist playlist = maybePlaylist.get();

            List<Song> resultList = playlist.getSonglist();

            List<Song> distinctResult = resultList.stream().distinct().collect(Collectors.toList());

            Profile profile = profileRepository.findByAccount(playlist.getAccount())
                    .orElseThrow(() -> new IllegalArgumentException("프로필 없음"));

            List<LikePlaylist> likePlaylists = likePlaylistRepository.findByPlaylist(playlist);

            return new PlaylistReadResponseForm(playlist.getPlaylistName(),
                    profile.getNickname(),
                    playlist.getThumbnailName(),
                    likePlaylists.size(),
                    distinctResult);
        }
        return null;
    }

    @Override
    @Transactional
    public long register(PlaylistRegisterRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return -1;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if (maybeAccount.isEmpty()) {
            return -1;
        }
        final Playlist playlist = new Playlist(requestForm.getPlaylistName(),
                requestForm.getThumbnailName(), maybeAccount.get());

        return playlistRepository.save(playlist).getPlaylistId();
    }

    @Override
    @Transactional
    public PlaylistModifyResponseForm modify(PlaylistModifyRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if (maybeAccount.isEmpty()) {
            return null;
        }
        Playlist playlist = playlistRepository.findById(requestForm.getPlaylistId())
                .orElseThrow(() -> new IllegalArgumentException("플레이리스트 없음"));

        if(playlist.getAccount().getAccountId().equals(accountId)) {
            playlist.setPlaylistName(requestForm.getPlaylistName());
            playlist.setThumbnailName(requestForm.getThumbnailName());
            playlistRepository.save(playlist);
            return new PlaylistModifyResponseForm(playlist.getPlaylistId(), playlist.getPlaylistName(), playlist.getThumbnailName());
        }
        return null;
    }


    @Override
    @Transactional
    public boolean delete(Long playlistId, HttpHeaders headers) {

        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);
        if (maybePlaylist.isEmpty()) {
            return false;
        }

        Playlist playlist = maybePlaylist.get();
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) { // authorization 키에 해당하는 값이 없을 경우 처리
            return false;
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계정 없음"));

        if (playlist.getAccount().getAccountId().equals(account.getAccountId())) {
            for(LikePlaylist likePlaylist : playlist.getLikePlaylist()){
                likePlaylistRepository.deleteById(likePlaylist.getLikePlaylistId());
            }
            playlistRepository.deleteById(playlistId);
            return true;
        }
        return false;
    }


    @Override
    @Transactional
    public List<MyPlaylistResponseForm> myPlaylist(HttpHeaders headers) {
        final List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return null;
        }

        final Long accountId = redisService.getValueByKey(authValues.get(0));
        final Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if (maybeAccount.isEmpty()) {
            return null;
        }

        final List<Playlist> playlists = playlistRepository.findByAccountId(accountId);

        final List<MyPlaylistResponseForm> responseForms = new ArrayList<>();
        for (Playlist playlist : playlists) {
            String thumbnailName = playlist.getThumbnailName();
            int likeCount = likePlaylistRepository.countByPlaylist(playlist);
            int songCount = playlist.getSonglist() != null ? playlist.getSonglist().size() : 0;

            // 썸네일을 등록하지 않았다면 유튜브 링크의 썸네일을 가져오도록
            if (thumbnailName == null && !playlist.getSonglist().isEmpty()) {
                thumbnailName = playlist.getSonglist().get(0).getLink();
            }

            MyPlaylistResponseForm responseForm = new MyPlaylistResponseForm(
                    playlist.getPlaylistId(), playlist.getPlaylistName(),
                    likeCount, songCount, thumbnailName);

            responseForms.add(responseForm);
        }
        return responseForms;
    }
}
