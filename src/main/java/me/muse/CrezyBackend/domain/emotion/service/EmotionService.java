package me.muse.CrezyBackend.domain.emotion.service;

import me.muse.CrezyBackend.domain.emotion.controller.form.AnalysisResponseForm;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface EmotionService {
    String analysis(String sentence);
    List<AnalysisResponseForm> recommendSong(String emotion) throws GeneralSecurityException, IOException;
}
