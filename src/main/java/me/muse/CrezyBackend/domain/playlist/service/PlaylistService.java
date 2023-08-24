package me.muse.CrezyBackend.domain.playlist.service;

import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistModifyRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistRegisterRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;

import org.springframework.http.HttpHeaders;
import java.util.List;

public interface PlaylistService {
    List<PlaylistResponseForm> list();
    PlaylistReadResponseForm read(Long playlistId);
    long register(PlaylistRegisterRequestForm requestForm);

    boolean modify(PlaylistModifyRequestForm requestForm);

    boolean delete(Long playlistId, HttpHeaders headers);
}
