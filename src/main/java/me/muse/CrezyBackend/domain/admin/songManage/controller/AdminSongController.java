package me.muse.CrezyBackend.domain.admin.songManage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.songManage.service.AdminSongService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-song")
public class AdminSongController {
    final private AdminSongService adminService;

    @GetMapping("/register-song-status-block")
    public Boolean registerSongStatus (@RequestParam("songId") Long songId, @RequestHeader HttpHeaders headers) {
        return adminService.registerSongStatus(songId, headers);
    }
}
