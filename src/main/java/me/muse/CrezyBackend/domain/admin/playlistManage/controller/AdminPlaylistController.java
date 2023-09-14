package me.muse.CrezyBackend.domain.admin.playlistManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSelectListForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistsRequestForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.todayStatusPlaylistResponseForm;
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
}
