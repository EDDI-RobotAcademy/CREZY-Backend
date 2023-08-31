package me.muse.CrezyBackend.domain.account.controller.form;

import lombok.Getter;

@Getter
public class AccountInfoResponseForm {
    private String nickname;
    private int myPlaylistCount;
    private int myLikedPlaylistCount;
    private String profileImageName;

    public AccountInfoResponseForm(String nickname, int myPlaylistCount, int myLikedPlaylistCount) {
        this.nickname = nickname;
        this.myPlaylistCount = myPlaylistCount;
        this.myLikedPlaylistCount = myLikedPlaylistCount;
    }

    public AccountInfoResponseForm(String nickname, int myPlaylistCount, int myLikedPlaylistCount, String profileImageName) {
        this.nickname = nickname;
        this.myPlaylistCount = myPlaylistCount;
        this.myLikedPlaylistCount = myLikedPlaylistCount;
        this.profileImageName = profileImageName;
    }
}
