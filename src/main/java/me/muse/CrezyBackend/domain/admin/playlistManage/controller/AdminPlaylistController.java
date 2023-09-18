package me.muse.CrezyBackend.domain.admin.playlistManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.*;
import me.muse.CrezyBackend.domain.admin.playlistManage.service.AdminPlaylistService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-playlist")
public class AdminPlaylistController {
    final private AdminPlaylistService adminService;
    @GetMapping("/check-playlist")
    public todayStatusPlaylistResponseForm todayStatusPlaylist(@RequestHeader HttpHeaders headers, @RequestParam("date") String date) {
        log.info("statusTodayPlaylist()");
        return adminService.todayStatusPlaylist(headers, date);
    }
    @PostMapping("/playlist-recent-list")
    public Page<AdminPlaylistSelectListForm> playlistRecentList(@RequestHeader HttpHeaders headers, @RequestBody AdminPlaylistsRequestForm requestForm) {
        log.info("playlistRecentList()");
        return adminService.playlistRecentList(headers, requestForm);
    }
    @GetMapping("/read-playlist")
    public AdminPlaylistReadResponseForm readPlaylist(@RequestHeader HttpHeaders headers, @RequestParam("playlistId") Long PlaylistId) {
        log.info("readPlaylist()");
        return adminService.readPlaylist(headers, PlaylistId);
    }
    @GetMapping("/change-playlistName")
    public void changePlaylistName(@RequestHeader HttpHeaders headers, @RequestParam("playlistId") Long playlistId) {
        log.info("changePlaylistName()");
        adminService.changePlaylistName(headers, playlistId);
    }
}
