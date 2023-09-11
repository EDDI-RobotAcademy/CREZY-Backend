package me.muse.CrezyBackend.domain.playlist.service;

import me.muse.CrezyBackend.domain.playlist.controller.form.*;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface PlaylistService {
    List<PlaylistResponseForm> list();
    PlaylistReadResponseForm read(Long playlistId);
    long register(PlaylistRegisterRequestForm requestForm, HttpHeaders headers);

    PlaylistModifyResponseForm modify(PlaylistModifyRequestForm requestForm, HttpHeaders headers);

    boolean delete(Long playlistId, HttpHeaders headers);

    List<MyPlaylistResponseForm> myPlaylist(HttpHeaders headers);

}
