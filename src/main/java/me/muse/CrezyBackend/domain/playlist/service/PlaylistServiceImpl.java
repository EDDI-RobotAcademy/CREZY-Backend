package me.muse.CrezyBackend.domain.playlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            String thumbnail = playlist.getThumbnailName();

            // 썸네일을 등록하지 않았다면 유튜브 링크의 썸네일을 가져오도록
            if (thumbnail == null && !playlist.getSongList().isEmpty()) {
                thumbnail = playlist.getSongList().get(0).getLink();
            }

            PlaylistResponseForm responseForm = new PlaylistResponseForm(
                    playlist.getId(), playlist.getPlaylistName(), playlist.getWriter(),
                    playlist.getLikers().size(), playlist.getSongList().size(), thumbnail);

            responseForms.add(responseForm);
        }
        return responseForms;
    }
}
