package me.muse.CrezyBackend.domain.admin.songManage.service;

import me.muse.CrezyBackend.domain.admin.songManage.controller.form.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AdminSongService {
    AdminSongDetailReadResponseForm readSongDetail(HttpHeaders headers, Long songId);

    Boolean registerSongStatusBlock(Long songId, HttpHeaders headers) throws GeneralSecurityException, IOException;

    Boolean registerSongStatusOpen(Long songId, HttpHeaders headers) throws GeneralSecurityException, IOException;

    Page<AdminSongListResponseForm> list(HttpHeaders headers, AdminSongListRequestForm requestForm);

    void modifyLyrics(HttpHeaders headers, AdminSongModifyLyricsRequestForm requestForm);

    void deleteSong(HttpHeaders headers, Long songId);

    TodayStatusSongResponseForm todayStatusSong(HttpHeaders headers, String date);

    Page<AdminSongListResponseForm> searchSong(HttpHeaders headers, AdminSongSearchRequestForm requestForm);
}
