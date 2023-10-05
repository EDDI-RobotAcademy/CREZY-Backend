package me.muse.CrezyBackend.domain.admin.playlistManage.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.account.entity.Profile;
import me.muse.CrezyBackend.domain.account.repository.ProfileRepository;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSelectListForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistsRequestForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.TodayStatusPlaylistResponseForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongDetailReadResponseForm;
import me.muse.CrezyBackend.domain.likePlaylist.entity.LikePlaylist;
import me.muse.CrezyBackend.domain.likePlaylist.repository.LikePlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistSearchRequestForm;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.utility.RandomValue;
import me.muse.CrezyBackend.utility.checkAdmin.CheckAdmin;
import me.muse.CrezyBackend.utility.transformToDate.TransformToDate;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminPlaylistServiceImpl implements AdminPlaylistService {
    final private PlaylistRepository playlistRepository;
    final private ProfileRepository profileRepository;
    final private LikePlaylistRepository likePlaylistRepository;
    final private SongRepository songRepository;
    final private Integer weeks = 6;
    final private CheckAdmin checkAdmin;

    @Override
    public TodayStatusPlaylistResponseForm todayStatusPlaylist(HttpHeaders headers, String date) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        Integer todayPlaylist = playlistRepository.findByCreateDate(TransformToDate.transformToDate(date)).size();
        Integer totalPlaylist = playlistRepository.findAllPlaylist().size();
        Integer previousPlaylist = playlistRepository.findByCreateDate((TransformToDate.transformToDate(date)).minusDays(1)).size();

        double increaseRate = 0;

        if (0 < todayPlaylist && previousPlaylist == 0) {
            increaseRate = 100;
        } else {
            increaseRate = (double) (todayPlaylist - previousPlaylist) / previousPlaylist * 100;
        }

        Integer afterDay = compareDate(TransformToDate.transformToDate(date));
        Integer previousDay = weeks - afterDay;

        LocalDate previousDate = (TransformToDate.transformToDate(date)).minusDays(previousDay);
        LocalDate afterDate = (TransformToDate.transformToDate(date)).plusDays(afterDay);

        List<Integer> playlistCounts = playlistBetweenPeriod(previousDate, afterDate);
        List<String> playlistDateList = playlistDateListBetweenPeriod(previousDate, afterDate);

        return new TodayStatusPlaylistResponseForm(todayPlaylist, totalPlaylist, (int) increaseRate, playlistCounts, playlistDateList);
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
        if (days < 3) {
            return days;
        }
        return 3;
    }

    public List<Integer> playlistBetweenPeriod(LocalDate previousDate, LocalDate afterDate) {
        List<Integer> playlistCounts = new ArrayList<>();

        while (!previousDate.isAfter(afterDate)) {
            Integer playlists = playlistRepository.findByCreateDate(previousDate).size();
            playlistCounts.add(playlists);
            previousDate = previousDate.plusDays(1);
        }
        return playlistCounts;
    }

    public List<String> playlistDateListBetweenPeriod(LocalDate previousDate, LocalDate afterDate) {
        List<String> playlistDateList = new ArrayList<>();

        while (!previousDate.isAfter(afterDate)) {
            playlistDateList.add(previousDate.toString());
            previousDate = previousDate.plusDays(1);
        }
        return playlistDateList;
    }

    @Override
    public Page<AdminPlaylistSelectListForm> playlistRecentList(HttpHeaders headers, AdminPlaylistsRequestForm requestForm) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        List<Playlist> playlists = new ArrayList<>();
        Pageable pageable = null;

        if (requestForm.getSortType().equals("recent")) {
            pageable = PageRequest.of(requestForm.getPage() - 1, 10);
            playlists = playlistRepository.findAllWithPage();
        } else if (requestForm.getSortType().equals("trending")) {
            pageable = PageRequest.of(requestForm.getPage() - 1, 10);
            playlists = playlistRepository.findAllSortByLikePlaylist();
        } else if (requestForm.getSortType().equals("empty")) {
            pageable = PageRequest.of(requestForm.getPage() - 1, 10);
            playlists = playlistRepository.findAllBySongEmpty();
        }

        final List<AdminPlaylistSelectListForm> adminPlaylistSelectListForms = new ArrayList<>();

        for (Playlist isPlaylist : playlists) {
            Profile isProfile = profileRepository.findByAccount_AccountId(isPlaylist.getAccount().getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("account 없음"));

            Integer likeCounts = likePlaylistRepository.countByPlaylist(isPlaylist);
            Integer songCounts = songRepository.countByPlaylist(isPlaylist);

            AdminPlaylistSelectListForm adminPlaylistSelectListForm =
                    new AdminPlaylistSelectListForm(isPlaylist.getPlaylistId(), isPlaylist.getPlaylistName(), isProfile.getNickname(), likeCounts, songCounts, isPlaylist.getCreateDate());
            adminPlaylistSelectListForms.add(adminPlaylistSelectListForm);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), adminPlaylistSelectListForms.size());

        return new PageImpl<>(
                adminPlaylistSelectListForms.subList(start, end),
                pageable,
                adminPlaylistSelectListForms.size()
        );
    }

    @Override
    @Transactional
    public AdminPlaylistReadResponseForm readPlaylist(HttpHeaders headers, Long playlistId) {
        if (!checkAdmin.checkAdmin(headers)) return null;

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("플레이리스트 없음"));
        Profile profile = profileRepository.findByAccount(playlist.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("프로필 없음"));

        List<AdminSongDetailReadResponseForm> songDetail = new ArrayList<>();
        List<Song> songlist = songRepository.findByPlaylist_PlaylistId(playlistId);

        for (Song song : songlist) {
            AdminSongDetailReadResponseForm songs =
                    new AdminSongDetailReadResponseForm(
                            song.getSongId(),
                            song.getTitle(),
                            song.getSinger(),
                            song.getCreateDate(),
                            song.getStatusType().getStatusType().toString()
                    );
            songDetail.add(songs);
        }

        List<LikePlaylist> likePlaylists = likePlaylistRepository.findByPlaylist(playlist);

        return new AdminPlaylistReadResponseForm(
                playlist.getPlaylistName(),
                profile.getNickname(),
                playlist.getThumbnailName(),
                playlist.getCreateDate(),
                likePlaylists.size(),
                songlist.size(),
                songDetail);
    }

    @Override
    public void changePlaylistName(HttpHeaders headers, Long playlistId) {
        if (!checkAdmin.checkAdmin(headers)) return;

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("playlist 없음"));

        String newPlaylistName = makeNewPlaylistName();

        playlist.setPlaylistName(newPlaylistName);
        playlistRepository.save(playlist);
    }

    private String makeNewPlaylistName() {
        String[] genreList = {"신나는", "감성적인", "힙한", "슬픈", "화난", "놀란", "행복한", "무서운"};

        RandomValue randomValue = new RandomValue();
        int value = randomValue.randomValue(genreList.length);

        String randomAlphabet = "";
        String randomNumber = "";

        for (int i = 0; i < 2; i++) {
            randomAlphabet += (String.valueOf((char) ((Math.random() * 26) + 65)));
            randomNumber += String.valueOf(randomValue.randomValue(9));
        }

        return genreList[value] + "CrezyList" + randomAlphabet + randomNumber;
    }

    @Override
    public void changePlaylistThumbnailName(HttpHeaders headers, Long playlistId) {
        if (!checkAdmin.checkAdmin(headers)) return;

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("playlist 없음"));

        playlist.setThumbnailName(null);
        playlistRepository.save(playlist);

    }

    @Override
    @Transactional
    public void deletePlaylist(HttpHeaders headers, Long playlistId) {
        if (!checkAdmin.checkAdmin(headers)) return;

        Playlist maybePlaylist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("playlist 없음"));

        for (LikePlaylist likePlaylist : maybePlaylist.getLikePlaylist()) {
            likePlaylistRepository.deleteById(likePlaylist.getLikePlaylistId());
        }
        playlistRepository.deleteById(playlistId);
    }

    @Override
    @Transactional
    public Page<AdminPlaylistSelectListForm> searchPlaylist(HttpHeaders headers, PlaylistSearchRequestForm requestForm) {
        if (!checkAdmin.checkAdmin(headers)) return null;
        List<Playlist> playlists = playlistRepository.findAllByPlaylistNameAndNickname(requestForm.getKeyword());

        Pageable pageable = PageRequest.of(requestForm.getPage() - 1, 10);
        List<AdminPlaylistSelectListForm> responseForms = new ArrayList<>();
        for (Playlist playlist : playlists) {
            String thumbnailName = playlist.getThumbnailName();
            int likeCount = playlist.getLikePlaylist() != null ? playlist.getLikePlaylist().size() : 0;
            int songCount = playlist.getSonglist() != null ? playlist.getSonglist().size() : 0;

            if (thumbnailName == null && !playlist.getSonglist().isEmpty()) {
                thumbnailName = playlist.getSonglist().get(0).getLink();
            }
            Profile profile = profileRepository.findByAccount(playlist.getAccount()).orElseThrow(() -> new IllegalArgumentException("프로필 없음"));
            AdminPlaylistSelectListForm responseForm = new AdminPlaylistSelectListForm(playlist.getPlaylistId(), playlist.getPlaylistName(), profile.getNickname(), likeCount, songCount, playlist.getCreateDate());

            responseForms.add(responseForm);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseForms.size());

        return new PageImpl<>(responseForms.subList(start, end), pageable, responseForms.size());
    }

}