package me.muse.CrezyBackend.domain.playlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistModifyRequestForm;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.entity.Account;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;

import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistRegisterRequestForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.entity.Playlist;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@RestController
public class PlaylistController {

    final private PlaylistService playlistService;
    final private PlaylistRepository playlistRepository;
    final private RedisService redisService;
    final private AccountRepository accountRepository;



    @GetMapping("/list")
    public List<PlaylistResponseForm> playList(){
        log.info("playList()");
        return playlistService.list();
    }

    @GetMapping("/{playlistId}")
    public PlaylistReadResponseForm readPlayList(@PathVariable("playlistId") Long playlistId) {
        log.info("readPlayList()");
        return playlistService.read(playlistId);
    }

    @PostMapping("/register")
    public long playlistRegister (@RequestBody PlaylistRegisterRequestForm requestForm) {
        log.info("playlistRegister()");
        return playlistService.register(requestForm);
    }

    @PostMapping("/modify")
    public boolean modifyPlaylist(@RequestBody PlaylistModifyRequestForm requestForm){
        return playlistService.modify(requestForm);
    }

    @DeleteMapping("/{playlistId}")
    public boolean deleteSong(@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {

        return playlistService.delete(playlistId, headers);
    }

}
