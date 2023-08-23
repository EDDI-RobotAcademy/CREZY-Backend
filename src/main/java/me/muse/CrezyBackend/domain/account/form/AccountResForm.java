package me.muse.CrezyBackend.domain.account.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccountResForm {
    private String name;
    private String nickname;
    private String email;
}
