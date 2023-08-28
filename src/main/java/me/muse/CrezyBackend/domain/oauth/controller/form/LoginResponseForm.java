package me.muse.CrezyBackend.domain.oauth.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponseForm {
    final private String nickname;
    final private String userToken;
}
