package me.muse.CrezyBackend.domain.admin.songManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongDetailReadResponseForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongListRequestForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongListResponseForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongModifyLyricsRequestForm;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.entity.SongStatusType;
import me.muse.CrezyBackend.domain.song.entity.StatusType;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.domain.song.repository.SongStatusRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static me.muse.CrezyBackend.domain.account.entity.RoleType.ADMIN;
import static me.muse.CrezyBackend.domain.song.entity.StatusType.BLOCK;
import static me.muse.CrezyBackend.domain.song.entity.StatusType.OPEN;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminSongServiceImpl implements AdminSongService{
    final private AccountRepository accountRepository;
    final private RedisService redisService;
    final private SongRepository songRepository;
    final private SongStatusRepository songStatusRepository;
    final private ProfileRepository profileRepository;

    @Override
    public AdminSongDetailReadResponseForm readSongDetail(HttpHeaders headers, Long songId) {
        if (!checkAdmin(headers)) return null;
        Song song= songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("노래 없음"));

        return new AdminSongDetailReadResponseForm(
                song.getSongId(),
                song.getTitle(),
                song.getSinger(),
                song.getCreateDate(),
                song.getLink(),
                song.getLyrics(),
                song.getBlockedDate(),
                song.getStatusType().getStatusType().toString());
    }

    @Override
    public Boolean registerSongStatusBlock(Long songId, HttpHeaders headers) {
        if (!checkAdmin(headers))
            return false;

        changeSongStatus(songId, BLOCK);
        return true;
    }
    @Override
    public Boolean registerSongStatusOpen(Long songId, HttpHeaders headers) {
        if (!checkAdmin(headers))
            return false;

        changeSongStatus(songId, OPEN);
        return true;
    }

    private void changeSongStatus(Long songId, StatusType statusType) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("노래가 존재하지 않습니다."));

        SongStatusType changeSongStatus = songStatusRepository.findByStatusType(statusType)
                .orElseThrow(() -> new IllegalArgumentException("상태 타입을 찾을 수 없습니다."));

        if (statusType == StatusType.OPEN) {
            song.setBlockedDate(null); 
        } else {
            song.setBlockedDate(LocalDate.now().toString());
        }

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

    @Override
    @Transactional
    public Page<AdminSongListResponseForm> list(HttpHeaders headers, AdminSongListRequestForm requestForm) {
        if (!checkAdmin(headers)) return null;

        List<Song> songList = new ArrayList<>();

        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10);

        if(requestForm.getSongStatusType().equals("TOTAL")){
            switch (requestForm.getSortType()){
                case "ASC" -> songList = songRepository.findAll(Sort.by(Sort.Direction.ASC, "songId"));
                case "DESC" -> songList = songRepository.findAll(Sort.by(Sort.Direction.DESC, "songId"));
            }
        }else {
            SongStatusType songStatusType = songStatusRepository.findByStatusType(StatusType.valueOf(requestForm.getSongStatusType()))
                    .orElseThrow(() -> new IllegalArgumentException("SongStatusType not found"));

            switch (requestForm.getSortType()){
                case "ASC" -> songList = songRepository.findByStatusTypeOrderBySongIdAsc(songStatusType);
                case "DESC" -> songList = songRepository.findByStatusTypeOrderBySongIdDesc(songStatusType);
            }
        }

        List<AdminSongListResponseForm> responseFormList = new ArrayList<>();

        for (Song song : songList) {
            Profile profile = profileRepository.findByAccount(song.getPlaylist().getAccount())
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

            AdminSongListResponseForm responseForm = new AdminSongListResponseForm(
                    song.getSongId(),
                    song.getTitle(),
                    song.getSinger(),
                    profile.getNickname(),
                    song.getCreateDate(),
                    song.getStatusType()
            );

            responseFormList.add(responseForm);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseFormList.size());

        return new PageImpl<>(
                responseFormList.subList(start, end),
                pageable,
                responseFormList.size()
        );
    }

    @Override
    public void modifyLyrics(HttpHeaders headers, AdminSongModifyLyricsRequestForm requestForm) {
        if (!checkAdmin(headers)) return;
        Song song = songRepository.findById(requestForm.getSongId())
                .orElseThrow(()-> new IllegalArgumentException("song 없음"));
        song.setLyrics(requestForm.getLyrics());
        songRepository.save(song);
    }

    @Override
    public void deleteSong(HttpHeaders headers, Long songId) {
        if (!checkAdmin(headers)) return;
        songRepository.deleteById(songId);
    }
}
