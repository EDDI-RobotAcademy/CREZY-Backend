package me.muse.CrezyBackend.domain.oauth.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginRequestForm {
    final private String code;
    final private String nickname;
    final private String profileImageName;
}
