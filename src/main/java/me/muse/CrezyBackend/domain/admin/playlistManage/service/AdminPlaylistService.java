package me.muse.CrezyBackend.domain.admin.playlistManage.service;

import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.todayStatusPlaylistResponseForm;
import org.springframework.http.HttpHeaders;

public interface AdminPlaylistService {
    todayStatusPlaylistResponseForm todayStatusPlaylist(HttpHeaders headers, String date);

}
