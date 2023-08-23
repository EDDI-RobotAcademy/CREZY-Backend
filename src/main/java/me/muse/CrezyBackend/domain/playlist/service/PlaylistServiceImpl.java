package me.muse.CrezyBackend.domain.playlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistRegisterRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.song.entity.Song;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService{

    final private PlaylistRepository playlistRepository;

    @Override
    @Transactional
    public List<PlaylistResponseForm> list() {
        List<Playlist> playlists = playlistRepository.findAll();

        List<PlaylistResponseForm> responseForms = new ArrayList<>();
        for (Playlist playlist : playlists) {
            String thumbnailName = playlist.getThumbnailName();
            int likeCount = playlist.getLikers() != null ? playlist.getLikers().size() : 0;
            int songCount = playlist.getSongList() != null ? playlist.getSongList().size() : 0;

            // 썸네일을 등록하지 않았다면 유튜브 링크의 썸네일을 가져오도록
            if (thumbnailName == null && !playlist.getSongList().isEmpty()) {
                thumbnailName = playlist.getSongList().get(0).getLink();
            }

            PlaylistResponseForm responseForm = new PlaylistResponseForm(
                    playlist.getPlaylistId(), playlist.getPlaylistName(), playlist.getWriter(),
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

            List<Song> resultList = playlist.getSongList();
            List<Song> distinctResult = resultList.stream().distinct().collect(Collectors.toList());

            return new PlaylistReadResponseForm(playlist.getPlaylistName(),
                    playlist.getWriter(),
                    playlist.getThumbnailName(),
                    distinctResult);
        }
        return null;
    }

    public long register(PlaylistRegisterRequestForm requestForm) {
        final Playlist playlist = playlistRepository.save(requestForm.toPlaylist());

        return playlistRepository.save(playlist).getPlaylistId();
    }
}
