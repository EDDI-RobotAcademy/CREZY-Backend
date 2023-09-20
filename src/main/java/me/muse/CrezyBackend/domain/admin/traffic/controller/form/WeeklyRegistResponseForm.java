package me.muse.CrezyBackend.domain.admin.traffic.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class WeeklyRegistResponseForm {
    final private int accountCount;
    final private int playlistCount;
    final private int songCount;
}