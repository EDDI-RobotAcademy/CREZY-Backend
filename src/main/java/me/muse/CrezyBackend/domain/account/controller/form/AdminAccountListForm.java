package me.muse.CrezyBackend.domain.account.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminAccountListForm {
    final private Long accountId;
    final private String nickname;
    final private Integer playlistCounts;
    final private Integer songCounts;
    final private LocalDate createDate;
    final private Integer warningCounts;
}
