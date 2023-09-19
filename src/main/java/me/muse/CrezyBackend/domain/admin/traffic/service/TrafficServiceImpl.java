package me.muse.CrezyBackend.domain.admin.traffic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.admin.traffic.entity.Traffic;
import me.muse.CrezyBackend.domain.admin.traffic.repository.TrafficRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrafficServiceImpl implements TrafficService{
    final private TrafficRepository trafficRepository;

    @Override
    public void analysisCounting(){
        Optional<Traffic> maybeTraffic = trafficRepository.findByDate(LocalDate.now());
        Traffic traffic;
        if(maybeTraffic.isEmpty()) {
            traffic = new Traffic(LocalDate.now());
            traffic.setAnalysisCount(1);
        }else {
            traffic = maybeTraffic.get();
            traffic.setAnalysisCount(traffic.getAnalysisCount()+1);
        }
        trafficRepository.save(traffic);
    }

    @Override
    public void loginCounting(){
        Optional<Traffic> maybeTraffic = trafficRepository.findByDate(LocalDate.now());
        Traffic traffic;

        if(maybeTraffic.isEmpty()) {
            traffic = new Traffic(LocalDate.now());
            traffic.setLoginCount(1);
        }else {
            traffic = maybeTraffic.get();
            traffic.setLoginCount(traffic.getLoginCount()+1);
        }
        trafficRepository.save(traffic);
    }
}
