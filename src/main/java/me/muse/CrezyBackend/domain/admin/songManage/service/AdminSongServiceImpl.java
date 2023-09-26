package me.muse.CrezyBackend.domain.admin.songManage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.*;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.entity.SongStatusType;
import me.muse.CrezyBackend.domain.song.entity.StatusType;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.domain.song.repository.SongStatusRepository;
import me.muse.CrezyBackend.utility.checkAdmin.CheckAdmin;
import me.muse.CrezyBackend.utility.transformToDate.TransformToDate;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static me.muse.CrezyBackend.domain.song.entity.StatusType.BLOCK;
import static me.muse.CrezyBackend.domain.song.entity.StatusType.OPEN;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminSongServiceImpl implements AdminSongService{
    final private SongRepository songRepository;
    final private SongStatusRepository songStatusRepository;
    final private ProfileRepository profileRepository;
    final private CheckAdmin checkAdmin;
    final private Integer weeks = 6;

    @Override
    public AdminSongDetailReadResponseForm readSongDetail(HttpHeaders headers, Long songId) {
        if (!checkAdmin.checkAdmin(headers)) return null;
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
        if (!checkAdmin.checkAdmin(headers)) return null;

        changeSongStatus(songId, BLOCK);
        return true;
    }
    @Override
    public Boolean registerSongStatusOpen(Long songId, HttpHeaders headers) {
        if (!checkAdmin.checkAdmin(headers)) return null;

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
            song.setLink(null);
        }

        song.setStatusType(changeSongStatus);
        songRepository.save(song);
    }

    @Override
    @Transactional
    public Page<AdminSongListResponseForm> list(HttpHeaders headers, AdminSongListRequestForm requestForm) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        List<Song> songList = new ArrayList<>();

        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10);

        if(requestForm.getSongStatusType().equals("TOTAL")){
            switch (requestForm.getSortType()){
                case "ASC" -> songList = songRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
                case "DESC" -> songList = songRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
            }
        }else {
            SongStatusType songStatusType = songStatusRepository.findByStatusType(StatusType.valueOf(requestForm.getSongStatusType()))
                    .orElseThrow(() -> new IllegalArgumentException("SongStatusType not found"));

            switch (requestForm.getSortType()){
                case "ASC" -> songList = songRepository.findByStatusTypeOrderByTitleAsc(songStatusType);
                case "DESC" -> songList = songRepository.findByStatusTypeOrderByTitleDesc(songStatusType);
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
        if (!checkAdmin.checkAdmin(headers)) return;

        Song song = songRepository.findById(requestForm.getSongId())
                .orElseThrow(()-> new IllegalArgumentException("song 없음"));
        song.setLyrics(requestForm.getLyrics());
        songRepository.save(song);
    }

    @Override
    @Transactional
    public void deleteSong(HttpHeaders headers, Long songId) {
        if (!checkAdmin.checkAdmin(headers)) return;
        songRepository.deleteById(songId);
    }

    @Override
    public TodayStatusSongResponseForm todayStatusSong(HttpHeaders headers, String date) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        Integer todaySong = songRepository.findByCreateDate(TransformToDate.transformToDate(date)).size();
        Integer totalSong = songRepository.findAll().size();
        Integer previousSong = songRepository.findByCreateDate((TransformToDate.transformToDate(date)).minusDays(1)).size();

        double increaseRate = 0;

        if(0 < todaySong && previousSong == 0){
            increaseRate = 100;
        }else {
            increaseRate = (double) (todaySong - previousSong) / previousSong * 100;
        }

        Integer afterDay = compareDate(TransformToDate.transformToDate(date));
        Integer previousDay = weeks-afterDay;

        LocalDate previousDate = (TransformToDate.transformToDate(date)).minusDays(previousDay);
        LocalDate afterDate = (TransformToDate.transformToDate(date)).plusDays(afterDay);

        List<Integer> playlistCounts = songBetweenPeriod(previousDate, afterDate);
        List<String> playlistDateList = songDateListBetweenPeriod(previousDate, afterDate);

        return new TodayStatusSongResponseForm(todaySong, totalSong, (int)increaseRate, playlistCounts, playlistDateList);
    }

    public Integer compareDate(LocalDate compareDate) {
        Long date = System.currentTimeMillis();

        SimpleDateFormat sdt = new SimpleDateFormat();
        sdt.applyPattern("yyyy-MM-dd");
        String currentDate = sdt.format(date);

        LocalDate transformCurrentDate = TransformToDate.transformToDate(currentDate);

        LocalDate date1 = transformCurrentDate;
        LocalDate date2 = compareDate;

        Period period = date2.until(date1);
        int days = period.getDays();
        if(days < 3) {
            return days;
        }
        return 3;
    }

    public List<Integer> songBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<Integer> songCounts = new ArrayList<>();

        while (!previousDate.isAfter(afterDate)) {
            Integer playlists = songRepository.findByCreateDate(previousDate).size();
            songCounts.add(playlists);
            previousDate = previousDate.plusDays(1);
        }
        return songCounts;
    }
    public List<String> songDateListBetweenPeriod(LocalDate previousDate, LocalDate afterDate){
        List<String> songDateList = new ArrayList<>();

        while (!previousDate.isAfter(afterDate)) {
            songDateList.add(previousDate.toString());
            previousDate = previousDate.plusDays(1);
        }
        return songDateList;
    }

    @Override
    @Transactional
    public Page<AdminSongListResponseForm> searchSong(HttpHeaders headers, AdminSongSearchRequestForm requestForm) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10);
        List<Song> songList = songRepository.findAllByTitleAndSinger(requestForm.getKeyword());

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
}
