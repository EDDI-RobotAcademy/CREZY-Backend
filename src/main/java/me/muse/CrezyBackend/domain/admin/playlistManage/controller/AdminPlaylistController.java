package me.muse.CrezyBackend.domain.admin.playlistManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSelectListForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistsRequestForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.TodayStatusPlaylistResponseForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.service.AdminPlaylistService;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistSearchRequestForm;
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
    public TodayStatusPlaylistResponseForm todayStatusPlaylist(@RequestHeader HttpHeaders headers, @RequestParam("date") String date) {
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

    @GetMapping("/change-playlistThumbnailName")
    public void changePlaylistThumbnailName(@RequestHeader HttpHeaders headers, @RequestParam("playlistId") Long playlistId) {
        log.info("changePlaylistThumbnailName()");
        adminService.changePlaylistThumbnailName(headers, playlistId);
    }

    @DeleteMapping("/delete-playlist")
    public void deletePlaylist(@RequestHeader HttpHeaders headers, @RequestParam("playlistId") Long playlistId) {
        log.info("deletePlaylist()");
        adminService.deletePlaylist(headers, playlistId);
    }

    @PostMapping("/search-playlist")
    public Page<AdminPlaylistSelectListForm> searchPlaylist(@RequestHeader HttpHeaders headers, @RequestBody PlaylistSearchRequestForm requestForm) {
        log.info("searchPlaylist()");
        return adminService.searchPlaylist(headers, requestForm);
    }
}
