package me.muse.CrezyBackend.domain.admin.songManage.controller.form;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.playlistManage.controller.form.AdminPlaylistSongDetailReadResponseForm;
import me.muse.CrezyBackend.domain.admin.playlistManage.service.AdminPlaylistService;
import me.muse.CrezyBackend.domain.admin.songManage.service.AdminSongService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin-song")
public class AdminSongController {
    final private AdminSongService adminService;

    @GetMapping("/read-song")
    public AdminPlaylistSongDetailReadResponseForm readSongDetail(@RequestHeader HttpHeaders headers, @RequestParam("songId") Long SongId) {
        log.info("readSongDetail()");
        return adminService.readSongDetail(headers, SongId);
    }
}
