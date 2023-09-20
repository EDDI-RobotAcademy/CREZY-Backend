package me.muse.CrezyBackend.domain.admin.traffic.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TodayTrafficCountResponseForm {
    final private int loginCount;
    final private int analysisCount;
    final private int reportCount;
    final private int inquiryCount;
    final private int loginCountRate;
    final private int analysisCountRate;
    final private int reportCountRate;
    final private int inquiryCountRate;
}
