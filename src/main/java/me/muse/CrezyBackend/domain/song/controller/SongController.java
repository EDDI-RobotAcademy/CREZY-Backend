package me.muse.CrezyBackend.domain.song.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.song.controller.form.SongRegisterRequestForm;
import me.muse.CrezyBackend.domain.song.service.SongService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {

    final private SongService songService;

    @PostMapping("/register")
    public Long songRegister (@RequestBody SongRegisterRequestForm requestForm) throws GeneralSecurityException, IOException {

        return songService.register(requestForm);
    }

    @DeleteMapping("/{songId}")
    public boolean deleteSong(@PathVariable("songId") Long songId, @RequestHeader HttpHeaders headers) {
        log.info("deleteSong() ");
        return songService.delete(songId, headers);
    }
}
