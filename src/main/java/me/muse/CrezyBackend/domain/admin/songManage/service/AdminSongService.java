package me.muse.CrezyBackend.domain.admin.songManage.service;

import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSongDetailReadResponseForm;
import org.springframework.http.HttpHeaders;

public interface AdminSongService {
    AdminPlaylistSongDetailReadResponseForm readSongDetail(HttpHeaders headers, Long songId);
    Boolean registerSongStatus(Long songId, HttpHeaders headers);

}
