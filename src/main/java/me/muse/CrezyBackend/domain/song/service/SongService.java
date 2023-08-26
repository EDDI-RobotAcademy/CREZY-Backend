package me.muse.CrezyBackend.domain.song.service;

import me.muse.CrezyBackend.domain.song.controller.form.SongModifyRequestForm;
import me.muse.CrezyBackend.domain.song.controller.form.SongRegisterRequestForm;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

import java.security.GeneralSecurityException;
import java.util.List;

public interface SongService {
    Long register(SongRegisterRequestForm requestForm) throws GeneralSecurityException, IOException;

    boolean delete(Long songId, HttpHeaders headers);

    boolean deleteSongIds(List<Long> songIds, HttpHeaders headers);

    boolean modify(SongModifyRequestForm requestForm, HttpHeaders headers);
}
