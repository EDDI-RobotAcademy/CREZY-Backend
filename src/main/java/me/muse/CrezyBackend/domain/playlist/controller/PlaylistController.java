package me.muse.CrezyBackend.domain.playlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.config.redis.service.RedisService;
import me.muse.CrezyBackend.domain.account.repository.AccountRepository;
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

    @PostMapping("/register")
    public long playlistRegister (@RequestBody PlaylistRegisterRequestForm requestForm, @RequestHeader HttpHeaders headers) {
        log.info("playlistRegister()");
        return playlistService.register(requestForm, headers);
    }

    @PostMapping("/modify")
    public boolean modifyPlaylist(@RequestBody PlaylistModifyRequestForm requestForm, @RequestHeader HttpHeaders headers){
        return playlistService.modify(requestForm, headers);
    }

    @DeleteMapping("/{playlistId}")
    public boolean deleteSong(@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {

        return playlistService.delete(playlistId, headers);
    }

    @PostMapping("/like-playlist/{playlistId}") // 플레이 리스트 좋아요
    public int likePlaylist (@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {
        return playlistService.likePlaylist(playlistId, headers);
    }

    @PostMapping("check-liked/{playlistId}") // 좋아요 유무 확인
    public boolean checkLikedPlaylist(@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {
        return playlistService.isPlaylistLiked(playlistId, headers);
    }

    @PostMapping("/unlike-playlist/{playlistId}") // 좋아요 해체
    public int unLikePlaylist (@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {
        return playlistService.unlikePlaylist(playlistId, headers);
    }

    @GetMapping("/my-playlist")
    public List<MyPlaylistResponseForm> myPlaylist(@RequestHeader HttpHeaders headers){
        log.info("myPlaylist()");
        return playlistService.myPlaylist(headers);
    }



}
