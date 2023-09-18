package me.muse.CrezyBackend.domain.admin.songManage.service;


import org.springframework.http.HttpHeaders;


public interface AdminSongService {
    Boolean registerSongStatus(Long songId, HttpHeaders headers);
}
