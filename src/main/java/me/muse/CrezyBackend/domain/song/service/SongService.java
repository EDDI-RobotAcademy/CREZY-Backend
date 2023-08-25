package me.muse.CrezyBackend.domain.song.service;

import me.muse.CrezyBackend.domain.song.controller.form.SongRegisterRequestForm;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface SongService {
    Long register(SongRegisterRequestForm requestForm) throws GeneralSecurityException, IOException;
}
