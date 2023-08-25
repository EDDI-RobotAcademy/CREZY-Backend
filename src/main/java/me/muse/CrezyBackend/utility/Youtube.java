package me.muse.CrezyBackend.utility;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@PropertySource("classpath:youtube.properties")
public class Youtube {

    private final String APPLICATION_NAME = "MyProject";


    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private NetHttpTransport httpTransport;

    private static YouTube youtube;

    @Value("${youtube.apikey}")
    private String youtubeApiKey;

    public String searchByKeyword(String keyword) throws IOException, GeneralSecurityException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        youtube = new YouTube.Builder(httpTransport, JSON_FACTORY, httpRequest -> {})
                .setApplicationName(APPLICATION_NAME)
                .build();

        String queryTerm = keyword;
        YouTube.Search.List search = youtube.search().list("id,snippet");
        search.setQ(queryTerm);
        search.setType("video");
        search.setMaxResults(10L);
        String apiKey = youtubeApiKey;
        search.setKey(apiKey);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();

        for (SearchResult searchResult : searchResultList) {
            System.out.println(searchResult.getId().getVideoId());
        }

        return searchResultList.get(0).getId().getVideoId();
    }

}