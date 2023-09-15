package me.muse.CrezyBackend.domain.emotion.service;

import me.muse.CrezyBackend.domain.song.entity.LabeledSong;

import java.util.List;

public interface EmotionService {
    String analysis(String sentence);
    List<LabeledSong> recommendSong(String emotion);
}
