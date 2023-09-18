package me.muse.CrezyBackend.domain.admin.songManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSongDetailReadResponseForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongListRequestForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongListResponseForm;
import me.muse.CrezyBackend.domain.admin.songManage.controller.form.AdminSongModifyLyricsRequestForm;
import me.muse.CrezyBackend.domain.admin.songManage.service.AdminSongService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-song")
public class AdminSongController {
    final private AdminSongService adminService;

    @GetMapping("/register-song-status-block")
    public Boolean registerSongStatusBlock (@RequestParam("songId") Long songId, @RequestHeader HttpHeaders headers) {
        return adminService.registerSongStatusBlock(songId, headers);
    }

    @GetMapping("/register-song-status-open")
    public Boolean registerSongStatusOpen (@RequestParam("songId") Long songId, @RequestHeader HttpHeaders headers) {
        return adminService.registerSongStatusOpen(songId, headers);
    }

    @GetMapping("/read-song")
    public AdminPlaylistSongDetailReadResponseForm readSongDetail(@RequestHeader HttpHeaders headers, @RequestParam("songId") Long SongId) {
        log.info("readSongDetail()");
        return adminService.readSongDetail(headers, SongId);
    }

    @PostMapping("/song-list")
    public Page<AdminSongListResponseForm> adminSongList(@RequestHeader HttpHeaders headers, @RequestBody AdminSongListRequestForm requestForm){
        return adminService.list(headers, requestForm);
    }
    @PostMapping("/modify-lyrics")
    public void modifyLyrics(@RequestHeader HttpHeaders headers, @RequestBody AdminSongModifyLyricsRequestForm requestForm){
        adminService.modifyLyrics(headers, requestForm);
    }
}
