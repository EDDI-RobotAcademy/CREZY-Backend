package me.muse.CrezyBackend.domain.admin.traffic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.TodayTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.controller.form.WeekTrafficCountResponseForm;
import me.muse.CrezyBackend.domain.admin.traffic.service.TrafficService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic")
public class TrafficController {
    final private TrafficService trafficService;

    @GetMapping(value = "/today-count")
    public TodayTrafficCountResponseForm todayCount(@RequestHeader HttpHeaders headers) {
        log.info("todayCount()");
        return trafficService.todayCount(headers);
    }

    @GetMapping(value = "/week-count")
    public WeekTrafficCountResponseForm weekCount(@RequestParam("weekValue") int weekValue, @RequestHeader HttpHeaders headers) {
        log.info("weekCount()");
        return trafficService.weekCount(weekValue, headers);
    }
}
