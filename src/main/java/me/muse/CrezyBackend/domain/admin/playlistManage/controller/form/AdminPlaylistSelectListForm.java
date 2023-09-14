package me.muse.CrezyBackend.domain.admin.playlistManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminPlaylistSelectListForm {
    final private Long playlistId;
    final private String playlistName;
    final private String accountName;
    final private Integer likeCounts;
    final private Integer songCounts;
    final private LocalDate createDate;
}
