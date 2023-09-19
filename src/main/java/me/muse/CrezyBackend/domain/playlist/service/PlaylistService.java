package me.muse.CrezyBackend.domain.playlist.service;

import me.muse.CrezyBackend.domain.playlist.controller.form.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface PlaylistService {
    Page<PlaylistResponseForm> list(Integer page);
    long register(PlaylistRegisterRequestForm requestForm, HttpHeaders headers);

    PlaylistModifyResponseForm modify(PlaylistModifyRequestForm requestForm, HttpHeaders headers);

    boolean delete(Long playlistId, HttpHeaders headers);

    List<MyPlaylistResponseForm> myPlaylist(HttpHeaders headers);

    PlaylistReadResponseForm readPlayList(Long playlistId);

    PlaylistReadResponseForm readMyPagePlaylist(Long playlistId);
}
