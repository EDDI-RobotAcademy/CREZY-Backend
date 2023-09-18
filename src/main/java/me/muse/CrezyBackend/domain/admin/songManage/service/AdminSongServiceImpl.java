package me.muse.CrezyBackend.domain.admin.songManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.entity.SongStatusType;
import me.muse.CrezyBackend.domain.song.entity.StatusType;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.domain.song.repository.SongStatusRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;
import static me.muse.CrezyBackend.domain.song.entity.StatusType.BLOCK;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSongServiceImpl implements AdminSongService {

    final private RedisService redisService;
    final private AccountRepository accountRepository;
    final private SongRepository songRepository;
    final private SongStatusRepository songStatusRepository;

    @Override
    public Boolean registerSongStatus(Long songId, HttpHeaders headers) {
        if (!checkAdmin(headers))
            return false;

        changeSongStatus(songId, BLOCK);
        return true;
    }

    private void changeSongStatus(Long songId, StatusType statusType) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("No such song exists"));

        SongStatusType changeSongStatus = songStatusRepository.findByStatusType(statusType).get();
        song.setStatusType(changeSongStatus);
        songRepository.save(song);
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
