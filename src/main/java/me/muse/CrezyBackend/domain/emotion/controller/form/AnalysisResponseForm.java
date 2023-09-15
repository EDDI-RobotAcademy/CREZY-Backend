package me.muse.CrezyBackend.domain.emotion.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AnalysisResponseForm {
    final private Long labeledSongId;
    final private String title;
    final private String singer;
    final private String link;
    final private String lyrics;
}
