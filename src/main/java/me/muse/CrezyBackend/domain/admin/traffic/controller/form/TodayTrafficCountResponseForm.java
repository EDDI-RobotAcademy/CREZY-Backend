package me.muse.CrezyBackend.domain.admin.traffic.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TodayTrafficCountResponseForm {
    final private int loginCount;
    final private int analysisCount;
}
