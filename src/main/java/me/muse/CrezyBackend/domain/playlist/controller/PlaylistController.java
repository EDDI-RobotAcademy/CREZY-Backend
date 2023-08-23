package me.muse.CrezyBackend.domain.playlist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistReadResponseForm;
import me.muse.CrezyBackend.domain.playlist.controller.form.PlaylistResponseForm;
import me.muse.CrezyBackend.domain.playlist.service.PlaylistService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/playlist")
@RestController
public class PlaylistController {

    final private PlaylistService playlistService;

    @GetMapping("/list")
    public List<PlaylistResponseForm> playList(){

        return playlistService.list();
    }

    @GetMapping("/{playlistId}")
    public PlaylistReadResponseForm readPlayList(@PathVariable("playlistId") Long playlistId){
        log.info("readPlayList()");
        return playlistService.read(playlistId);
    }
}
