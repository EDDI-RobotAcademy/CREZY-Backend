package me.muse.CrezyBackend.domain.account.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminAccountDetailForm {
    final private Long accountId;
    final private String nickname;
    final private Integer warningCounts;
    final private Integer reportedCounts;
    final private LocalDate lastLoginDate;
    final private Integer playlistCounts;
    final private Integer songCounts;
    final private Integer likePlaylistCounts;
}
