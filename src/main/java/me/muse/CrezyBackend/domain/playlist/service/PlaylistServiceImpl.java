package me.muse.CrezyBackend.domain.playlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistModifyRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistRegisterRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpHeaders;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService{

    final private PlaylistRepository playlistRepository;
    final private RedisService redisService;
    final private AccountRepository accountRepository;

    @Override
    @Transactional
    public List<PlaylistResponseForm> list() {
        List<Playlist> playlists = playlistRepository.findAll();

        List<PlaylistResponseForm> responseForms = new ArrayList<>();
        for (Playlist playlist : playlists) {
            String thumbnailName = playlist.getThumbnailName();
            int likeCount = playlist.getLikers() != null ? playlist.getLikers().size() : 0;
            int songCount = playlist.getSonglist() != null ? playlist.getSonglist().size() : 0;

            // 썸네일을 등록하지 않았다면 유튜브 링크의 썸네일을 가져오도록
            if (thumbnailName == null && !playlist.getSonglist().isEmpty()) {
                thumbnailName = playlist.getSonglist().get(0).getLink();
            }

            PlaylistResponseForm responseForm = new PlaylistResponseForm(
                    playlist.getPlaylistId(), playlist.getPlaylistName(), playlist.getAccount().getNickname(),
                    likeCount, songCount, thumbnailName);

            responseForms.add(responseForm);
        }
        return responseForms;
    }

    @Override
    @Transactional
    public PlaylistReadResponseForm read(Long playlistId) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);
        if (maybePlaylist.isPresent()) {
            Playlist playlist = maybePlaylist.get();

            List<Song> resultList = playlist.getSonglist();
            List<Song> distinctResult = resultList.stream().distinct().collect(Collectors.toList());

            return new PlaylistReadResponseForm(playlist.getPlaylistName(),
                    playlist.getAccount().getNickname(),
                    playlist.getThumbnailName(),
                    distinctResult);
        }
        return null;
    }

    public long register(PlaylistRegisterRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return -1;
        }
        Long userId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(userId);
        if (maybeAccount.isEmpty()) {
            return -1;
        }
        final Playlist playlist = new Playlist(requestForm.getPlaylistName(),
                requestForm.getThumbnailName(), maybeAccount.get());

        return playlistRepository.save(playlist).getPlaylistId();
    }

    @Override
    @Transactional
    public boolean modify(PlaylistModifyRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long userId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> maybeAccount = accountRepository.findById(userId);
        if (maybeAccount.isEmpty()) {
            return false;
        }
        Playlist playlist = playlistRepository.findById(requestForm.getPlaylistId())
                .orElseThrow(() -> new IllegalArgumentException("플레이리스트 없음"));

        if(playlist.getAccount().getAccountId().equals(userId)) {
            playlist.setPlaylistName(requestForm.getPlaylistName());
            playlist.setThumbnailName(requestForm.getThumbnailName());
            playlistRepository.save(playlist);
            return true;
        }
        return false;


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
        Optional<Account> isAccount = accountRepository.findById(accountId);
        if(isAccount.isEmpty()){
            return false;
        }

        if (playlist.getAccount().getAccountId().equals(accountId)) {
            playlistRepository.deleteById(playlistId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public int likePlaylist(Long playlistId, HttpHeaders headers) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);

        if (maybePlaylist.isEmpty()) {
            return 0;
        }

        Playlist playlist = maybePlaylist.get();

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return playlist.getLikers().size();
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        Optional<Account> isAccount = accountRepository.findById(accountId);

        if(isAccount.isEmpty()){
            return playlist.getLikers().size();
        }

        Account account = isAccount.get();

        account.getLikedPlaylists().add(playlist);
        accountRepository.save(account);
        playlist.getLikers().add(account);
        playlistRepository.save(playlist);

        return playlist.getLikers().size();
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

        Optional<Account> isAccount = accountRepository.findById(accountId);

        if(isAccount.isEmpty()){
            return false;
        }

        Account account = isAccount.get();

        Set<Playlist> likedPlaylists = account.getLikedPlaylists();

        return likedPlaylists.contains(playlist); // 안에 포함 되어 있으면 true 반환

    }

    @Override
    @Transactional
    public int unlikePlaylist(Long playlistId, HttpHeaders headers) {
        Optional<Playlist> maybePlaylist = playlistRepository.findById(playlistId);

        if (maybePlaylist.isEmpty()) {
            return 0;
        }

        Playlist playlist = maybePlaylist.get();

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return playlist.getLikers().size();
        }

        Long accountId = redisService.getValueByKey(authValues.get(0));

        Optional<Account> isAccount = accountRepository.findById(accountId);

        if(isAccount.isEmpty()){
            return playlist.getLikers().size();
        }

        Account account = isAccount.get();

        account.getLikedPlaylists().remove(playlist);
        accountRepository.save(account);
        playlist.getLikers().remove(account);
        playlistRepository.save(playlist);

        return playlist.getLikers().size();
    }


}
