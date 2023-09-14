package me.muse.CrezyBackend.domain.admin.accountManage.controller.form;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AdminAccountListForm {
    private Long accountId;
    private String nickname;
    private Integer playlistCounts;
    private Integer songCounts;
    private LocalDate createDate;
    private Integer warningCounts;
    private String accountRoleType;

    public AdminAccountListForm(Long accountId, String nickname, Integer playlistCounts, Integer songCounts, LocalDate createDate, Integer warningCounts) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.playlistCounts = playlistCounts;
        this.songCounts = songCounts;
        this.createDate = createDate;
        this.warningCounts = warningCounts;
    }

    public AdminAccountListForm(Long accountId, String nickname, Integer playlistCounts, Integer songCounts, LocalDate createDate, Integer warningCounts, String accountRoleType) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.playlistCounts = playlistCounts;
        this.songCounts = songCounts;
        this.createDate = createDate;
        this.warningCounts = warningCounts;
        this.accountRoleType = accountRoleType;
    }
}

