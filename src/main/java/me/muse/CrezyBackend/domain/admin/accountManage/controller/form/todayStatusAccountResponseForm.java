package me.muse.CrezyBackend.domain.admin.accountManage.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class todayStatusAccountResponseForm {
    final private Integer todayAccount;
    final private Integer totalAccount;
    final private Integer increaseRate;
    final private List<Integer> accountCounts;
    final private List<String> accountDateList;
}
