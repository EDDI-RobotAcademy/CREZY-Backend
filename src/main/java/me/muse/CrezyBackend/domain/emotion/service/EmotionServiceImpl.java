package me.muse.CrezyBackend.domain.emotion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.emotion.controller.form.AnalysisRequestForm;
import me.muse.CrezyBackend.domain.emotion.controller.form.AnalysisResponseForm;
import me.muse.CrezyBackend.domain.song.entity.LabeledSong;
import me.muse.CrezyBackend.domain.song.repository.LabeledSongRepository;
import me.muse.CrezyBackend.utility.RandomValue;
import me.muse.CrezyBackend.utility.Youtube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:youtube.properties")
public class EmotionServiceImpl implements EmotionService{

    final private LabeledSongRepository labeledSongRepository;
    final private Youtube youtube;

    @Value("${youtube.lyricsAddress}")
    private String lyricsAddress;

    @Override
    public String analysis(String sentence) {
        String url = "http://" + lyricsAddress + "/ai-request-command";

        RestTemplate restTemplate = new RestTemplate();

        AnalysisRequestForm requestForm = new AnalysisRequestForm(3, ","+sentence);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AnalysisRequestForm> requestEntity = new HttpEntity<>(requestForm, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String emotion = response.getBody();
            if (Objects.equals(emotion, "true")) {
                log.info(emotion);
                return result_emotion(requestEntity);

            } else {
                return "감정을 찾지 못했습니다.";
            }
        } else {
            return "요청 실패";
        }
    }

    public String result_emotion(HttpEntity<AnalysisRequestForm> requestFormHttpEntity) {
        String result_url = "http://" + lyricsAddress + "/ai-response";

        RestTemplate restTemplate = new RestTemplate();

        try {
            Thread.sleep(3000); // 3초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //ai-response
        ResponseEntity<String> result_response = restTemplate.exchange(result_url, HttpMethod.GET, requestFormHttpEntity,
                String.class);
        if (result_response.getStatusCode().is2xxSuccessful()) {
            return result_response.getBody();
        } else {
            return "요청 실패";
        }
    }

    @Override
    public List<AnalysisResponseForm> recommendSong(String emotion) throws GeneralSecurityException, IOException {
        emotion = emotion.replaceAll("\"", "");
        log.info("emotion : {}", emotion);
        String label = switch (emotion) {
            case "공포" -> "0";
            case "놀람" -> "1";
            case "분노" -> "2";
            case "슬픔" -> "3";
            case "중립" -> "4";
            case "행복" -> "5";
            case "혐오" -> "6";
            default -> null;
        };

        log.info("label : {}", label);

        List<LabeledSong> labeledSongList = labeledSongRepository.findByLabel(label);
        List<AnalysisResponseForm> responseFormList = new ArrayList<>();

        RandomValue random = new RandomValue();

        int[] valueArray = random.randomValueList(labeledSongList.size());
        for(int i : valueArray){
            LabeledSong labeledSong = labeledSongList.get(i);
            String videoId = youtube.searchByKeyword(labeledSong.getTitle() + " " + labeledSong.getArtist());
            
            responseFormList.add(new AnalysisResponseForm(labeledSong.getLabeledSongId(), labeledSong.getTitle(), labeledSong.getArtist(),
                    "https://www.youtube.com/watch?v=" + videoId, labeledSong.getLyrics()));
        }

        return responseFormList;
    }
}