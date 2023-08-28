package me.muse.CrezyBackend.domain.song.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.controller.form.SongModifyRequestForm;
import me.muse.CrezyBackend.domain.song.controller.form.SongRegisterRequestForm;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.utility.Youtube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService{

    final private PlaylistRepository playlistRepository;
    final private SongRepository songRepository;
    final private Youtube youtube;
    final private RedisService redisService;
    final private AccountRepository accountRepository;

    @Value("${youtube.lyricsAddress}")
    private String lyricsAddress;

    @Override
    @Transactional
    public Long register(SongRegisterRequestForm requestForm, HttpHeaders headers) throws GeneralSecurityException, IOException {

        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return 0L;
        }

        Long userId = redisService.getValueByKey(authValues.get(0));

        final Playlist playlist = playlistRepository.findWithSongById(requestForm.getPlaylistId());

        if(!playlist.getAccount().getAccountId().equals(userId)){
            return null;
        }

        final Song song = new Song(requestForm.getTitle(), requestForm.getSinger(), requestForm.getGenre(), requestForm.getLink(), playlist);
        if(requestForm.getLink().equals("")){
            String videoId = youtube.searchByKeyword(requestForm.getSinger() + " " + requestForm.getTitle());
            song.setLink("https://www.youtube.com/watch?v=" + videoId);
        }

//        song.setLyrics(getLyrics(requestForm.getSinger() + " " + requestForm.getTitle()));

        songRepository.save(song);

        return song.getSongId();
    }

    @Override
    @Transactional
    public boolean delete(Long songId, HttpHeaders headers) {

        Optional<Song> maybeSong = songRepository.findById(songId);
        if (maybeSong.isEmpty()) {
            return false;
        }
        Song song = maybeSong.get();
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));

        if (authValues.isEmpty()) {
            return false;
        }

        Long userId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> isAccount = accountRepository.findById(userId);
        if (isAccount.isEmpty()) {
            return false;
        }
        if (song.getPlaylist().getAccount().getAccountId().equals(userId)) {
            songRepository.deleteById(songId);
            return true;
        }
        return false;
    }

    public String getLyrics(String searchWord) {
        String url = "http://" + lyricsAddress + "/get_lyrics?song_title=" + searchWord;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String lyrics = null;
        if (response.hasBody()) {
            lyrics = response.getBody();
            if (lyrics != null) {
                log.info(lyrics);
            } else {
                return "가사를 못 찾았어용";
            }
        }
        lyrics = lyrics.replaceAll("\"", "");

        return lyrics;
    }

    @Override
    @Transactional
    public boolean deleteSongIds(List<Long> songIds, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long userId = redisService.getValueByKey(authValues.get(0));
        Optional<Account> isAccount = accountRepository.findById(userId);
        if (isAccount.isEmpty()) {
            return false;
        }
        for(Long id : songIds){
            Song song= songRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("노래 없음"));

            if(!song.getPlaylist().getAccount().getAccountId().equals(userId)){
                continue;
            }

            songRepository.deleteById(id);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean modify(SongModifyRequestForm requestForm, HttpHeaders headers) {
        List<String> authValues = Objects.requireNonNull(headers.get("authorization"));
        if (authValues.isEmpty()) {
            return false;
        }
        Long userId = redisService.getValueByKey(authValues.get(0));
        Song song= songRepository.findById(requestForm.getSongId())
                .orElseThrow(() -> new IllegalArgumentException("노래 없음"));
        if(song.getPlaylist().getAccount().getAccountId().equals(userId)) {
            song.setLink(requestForm.getLink());
            songRepository.save(song);
            return true;
        }
        return false;
    }
}
