package me.muse.CrezyBackend.domain.admin.songManage.service;

import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSongDetailReadResponseForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongListRequestForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongListResponseForm;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public interface AdminSongService {
    AdminPlaylistSongDetailReadResponseForm readSongDetail(HttpHeaders headers, Long songId);
    Boolean registerSongStatusBlock(Long songId, HttpHeaders headers);
    Boolean registerSongStatusOpen(Long songId, HttpHeaders headers);
    Page<AdminSongListResponseForm> list(HttpHeaders headers, AdminSongListRequestForm requestForm);
}
