package me.muse.CrezyBackend.domain.emotion.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.emotion.controller.form.AnalysisResponseForm;
import me.muse.CrezyBackend.domain.emotion.service.EmotionService;
import me.muse.CrezyBackend.domain.song.entity.LabeledSong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/emotion")
public class EmotionController {
    final private EmotionService emotionService;

    @GetMapping("/analysis")
    public List<AnalysisResponseForm> emotion_analysis(@RequestParam("sentence") String sentence) throws GeneralSecurityException, IOException {
        String emotion = emotionService.analysis(sentence);
        return emotionService.recommendSong(emotion);
    }
}
