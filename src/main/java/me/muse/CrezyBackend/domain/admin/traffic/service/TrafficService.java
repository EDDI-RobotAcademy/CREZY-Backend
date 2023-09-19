package me.muse.CrezyBackend.domain.admin.traffic.service;

import me.muse.CrezyBackend.domain.admin.traffic.controller.form.TodayTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.WeekTrafficCountResponseForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface TrafficService {
    void analysisCounting();
    void loginCounting();

    TodayTrafficCountResponseForm todayCount(HttpHeaders headers);

    WeekTrafficCountResponseForm weekCount(int weekValue, HttpHeaders headers);
}
