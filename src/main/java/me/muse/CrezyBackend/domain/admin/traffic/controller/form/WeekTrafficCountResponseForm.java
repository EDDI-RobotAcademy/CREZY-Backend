package me.muse.CrezyBackend.domain.admin.traffic.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class WeekTrafficCountResponseForm {
    final private List<Integer> loginCount;
    final private List<Integer> analysisCount;
}
