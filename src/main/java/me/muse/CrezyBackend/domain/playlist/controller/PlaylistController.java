package me.muse.CrezyBackend.domain.playlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
import me.muse.CrezyBackend.domain.likePlaylist.Service.LikePlaylistService;
import me.muse.CrezyBackend.domain.playlist.controller.form.*;
import me.muse.CrezyBackend.domain.playlist.repository.PlaylistRepository;
import me.muse.CrezyBackend.domain.playlist.service.PlaylistService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/register") // 플레이 리스트 등록
    public long playlistRegister (@RequestBody PlaylistRegisterRequestForm requestForm, @RequestHeader HttpHeaders headers) {
        log.info("playlistRegister()");
        return playlistService.register(requestForm, headers);
    }

    @PostMapping("/modify") // 플레이 리스트 수정
    public PlaylistModifyResponseForm modifyPlaylist(@RequestBody PlaylistModifyRequestForm requestForm, @RequestHeader HttpHeaders headers){
        return playlistService.modify(requestForm, headers);
    }

    @DeleteMapping("/{playlistId}") // 플레이 리스트 삭제
    public boolean deleteSong(@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {

        return playlistService.delete(playlistId, headers);
    }

    @GetMapping("/my-playlist") // 내가 등록한 플레이 리스트
    public List<MyPlaylistResponseForm> myPlaylist(@RequestHeader HttpHeaders headers){
        log.info("myPlaylist()");
        return playlistService.myPlaylist(headers);
    }

}
