package me.muse.CrezyBackend.domain.admin.accountManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AdminAccountDetailForm {
    final private Long accountId;
    final private String nickname;
    final private String email;
    final private Integer warningCounts;
    final private Integer reportedCounts;
    final private LocalDate lastLoginDate;
    final private Integer playlistCounts;
    final private Integer songCounts;
    final private Integer likePlaylistCounts;
    final private List<Integer> playlistCountsList;
    final private List<Integer> songCountsList;
    final private List<String> accountDateList;
    final private String accountRoleType;
}
