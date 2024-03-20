package com.apollotune.server.services.impl;

import com.apollotune.server.services.BraveSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BraveSearchServiceImpl implements BraveSearchService {

    @Value("${brave-api.end-point}")
    private String apiUrl;
    @Value("${brave-api.api-key}")
    private String apiKey;

    @Override
    public ResponseEntity<String> performSearch(String musicName, String artistName) {
        String urlWithSong ="spotify+track+link+of+song+" + musicName + "+by+" + artistName + "+insite:open.spotify.com" + "&api_key=";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("q",urlWithSong);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Accept-Encoding", "gzip");
        headers.set("x-subscription-token", apiKey);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriBuilder.toUriString(),String.class);
        return ResponseEntity.ok().headers(headers).build();
    }
}
