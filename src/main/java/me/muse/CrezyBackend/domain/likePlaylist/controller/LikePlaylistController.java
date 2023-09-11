package me.muse.CrezyBackend.domain.likePlaylist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.likePlaylist.Service.LikePlaylistService;
import me.muse.CrezyBackend.domain.likePlaylist.controller.form.PlaylistUsersLikeResponseForm;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/likePlaylist")
@RestController
public class LikePlaylistController {
    final private LikePlaylistService likePlaylistService;

    @PostMapping("/like-playlist/{playlistId}") // 플레이 리스트 좋아요
    public int likePlaylist (@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {
        return likePlaylistService.likePlaylist(playlistId, headers);
    }

    @PostMapping("check-liked/{playlistId}") // 좋아요 유무 확인
    public boolean checkLikedPlaylist(@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {
        return likePlaylistService.isPlaylistLiked(playlistId, headers);
    }

    @PostMapping("/unlike-playlist/{playlistId}") // 좋아요 해체
    public int unLikePlaylist (@PathVariable("playlistId") Long playlistId, @RequestHeader HttpHeaders headers) {
        return likePlaylistService.unlikePlaylist(playlistId, headers);
    }

    @GetMapping("/my-liked-playlist") // 사용자가 좋아요 한 목록 가져오기
    public List<PlaylistUsersLikeResponseForm> myLikedPlaylist(@RequestHeader HttpHeaders headers) {
        return likePlaylistService.bringLikePlaylist(headers);
    }
}
