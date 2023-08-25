package me.muse.CrezyBackend.domain.song.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.controller.form.SongRegisterRequestForm;
import me.muse.CrezyBackend.domain.song.entity.Song;
import me.muse.CrezyBackend.domain.song.repository.SongRepository;
import me.muse.CrezyBackend.utility.Youtube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;


@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService{

    final private PlaylistRepository playlistRepository;
    final private SongRepository songRepository;
    final private Youtube youtube;

    @Value("${youtube.lyricsAddress}")
    private String lyricsAddress;

    @Override
    public Long register(SongRegisterRequestForm requestForm) throws GeneralSecurityException, IOException {

        final Playlist playlist = playlistRepository.findWithSongById(requestForm.getPlaylistId());
        final Song song = new Song(requestForm.getTitle(), requestForm.getSinger(), requestForm.getGenre(), requestForm.getLink(), playlist);
        if(requestForm.getLink().equals("")){
            String videoId = youtube.searchByKeyword(requestForm.getSinger() + " " + requestForm.getTitle());
            song.setLink("https://www.youtube.com/watch?v=" + videoId);
        }

//        song.setLyrics(getLyrics(requestForm.getSinger() + " " + requestForm.getTitle()));

        songRepository.save(song);

        return song.getSongId();

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
}
