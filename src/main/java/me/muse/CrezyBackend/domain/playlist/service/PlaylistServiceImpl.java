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
            PlaylistResponseForm responseForm = new PlaylistResponseForm(
                    playlist.getId(), playlist.getName(), playlist.getWriter(),
                    playlist.getLikeCount().size(), playlist.getSongCount(), playlist.getThumbnailName());

            // 썸네일을 등록하지 않았다면 유튜브 링크의 썸네일을 가져오도록 (추후 수정)
            if (playlist.getThumbnailName() == null) {
                return null;
            }
            responseForms.add(responseForm);
        }
        return responseForms;
    }
}
