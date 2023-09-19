package me.muse.CrezyBackend.domain.admin.traffic.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class WeeklyRegistResponseForm {
    final private List<Integer> accountCount;
    final private List<Integer> playlistCount;
    final private List<Integer> songCount;
}
