package me.muse.CrezyBackend.domain.admin.playlistManage.service;

import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSelectListForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistsRequestForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.TodayStatusPlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistSearchRequestForm;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public interface AdminPlaylistService {
    TodayStatusPlaylistResponseForm todayStatusPlaylist(HttpHeaders headers, String date);

    Page<AdminPlaylistSelectListForm> playlistRecentList(HttpHeaders headers, AdminPlaylistsRequestForm requestForm);

    AdminPlaylistReadResponseForm readPlaylist(HttpHeaders headers, Long playlistId);

    void changePlaylistName(HttpHeaders headers, Long playlistId);

    void changePlaylistThumbnailName(HttpHeaders headers, Long playlistId);

    void deletePlaylist(HttpHeaders headers, Long playlistId);

    Page<PlaylistResponseForm> searchPlaylist(HttpHeaders headers, PlaylistSearchRequestForm requestForm);
}
