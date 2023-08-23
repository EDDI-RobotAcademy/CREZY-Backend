package me.muse.CrezyBackend.domain.account.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountDto {
    private String name;
    private String nickname;
    private String password;
    private String email;

    @Builder
    public AccountDto(String name, String nickname, String password, String email) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }
}
