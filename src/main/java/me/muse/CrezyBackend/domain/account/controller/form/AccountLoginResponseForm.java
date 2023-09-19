package me.muse.CrezyBackend.domain.account.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountLoginResponseForm {
    final private String nickname;
    final private String roleType;
    final private String userToken;
}
