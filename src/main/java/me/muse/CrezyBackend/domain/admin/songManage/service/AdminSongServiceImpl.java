package me.muse.CrezyBackend.domain.admin.songManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSongDetailReadResponseForm;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminSongServiceImpl implements AdminSongService{
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private SongRepository songRepository;
    @Override
    public AdminPlaylistSongDetailReadResponseForm readSongDetail(HttpHeaders headers, Long songId) {
        if (!checkAdmin(headers)) return null;
        Song song= songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("노래 없음"));

        return new AdminPlaylistSongDetailReadResponseForm(
                song.getSongId(),
                song.getTitle(),
                song.getSinger(),
                song.getCreateDate(),
                song.getLink(),
                song.getLyrics());
    }
    private boolean checkAdmin(HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long accountId = redisService.getValueByKey(authValues.get(0));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getRoleType().getRoleType() != ADMIN) {
            return false;
        }
        return true;
    }

}
