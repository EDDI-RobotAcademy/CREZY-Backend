package me.muse.CrezyBackend.domain.playlist.service;

import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;

import java.util.List;

public interface PlaylistService {
    List<PlaylistResponseForm> list();

    PlaylistReadResponseForm read(Long playlistId);
}
