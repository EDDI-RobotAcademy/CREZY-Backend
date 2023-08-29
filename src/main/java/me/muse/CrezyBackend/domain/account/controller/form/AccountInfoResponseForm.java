package me.muse.CrezyBackend.domain.account.controller.form;

import lombok.Getter;

@Getter
public class AccountInfoResponseForm {
    private String nickname;
    private int myPlaylistCount;
    private int myLikedPlaylistCount;

    public AccountInfoResponseForm(String nickname, int myPlaylistCount, int myLikedPlaylistCount) {
        this.nickname = nickname;
        this.myPlaylistCount = myPlaylistCount;
        this.myLikedPlaylistCount = myLikedPlaylistCount;
    }
}
