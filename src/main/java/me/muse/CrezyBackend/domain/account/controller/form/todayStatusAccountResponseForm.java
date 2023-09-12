package me.muse.CrezyBackend.domain.account.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class todayStatusAccountResponseForm {
    final private Integer todayAccount;
    final private Integer totalAccount;
    final private Integer increaseRate;
}
