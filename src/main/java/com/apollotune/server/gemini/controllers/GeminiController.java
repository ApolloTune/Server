package com.apollotune.server.gemini.controllers;


import com.apollotune.server.exceptions.ApiException;
import com.apollotune.server.gemini.payloads.request.GeminiKeySearchRequest;
import com.apollotune.server.gemini.payloads.response.GeminiKeySearchResponse;
import com.apollotune.server.gemini.payloads.response.GeminiKeySearchResponseWithSpotify;
import com.apollotune.server.gemini.prompt.GeminiPromptByKeySearch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.neovisionaries.i18n.CountryCode;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
public class GeminiController {

    @Value("${gemini.model}")
    private String MODEL;

    @Value("${gemini.project}")
    private String PROJECT;

    @Value("${gemini.api.location}")
    private String LOCATION;

    @Value("${spotify.client-secret-id}")
    private String SPOTIFY_CLIENT_SECRET_ID;

    @Value("${spotify.client-id}")
    private String SPOTIFY_CLIENT_ID;

    @Nullable
    private List<GeminiKeySearchResponseWithSpotify> getKeySearchResponseWithSpotifies(List<GeminiKeySearchResponse> geminiKeySearchResponses, List<GeminiKeySearchResponseWithSpotify> geminiKeySearchResponseWithSpotifies) {
        try {
            SpotifyApi spotifyApi = new SpotifyApi
                    .Builder()
                    .setClientId(SPOTIFY_CLIENT_ID)
                    .setClientSecret(SPOTIFY_CLIENT_SECRET_ID)
                    .build();
            ClientCredentialsRequest clientCredentialsRequest = spotifyApi
                    .clientCredentials()
                    .build();
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            for (GeminiKeySearchResponse response : geminiKeySearchResponses) {
                SearchTracksRequest searchTracksRequest = spotifyApi
                        .searchTracks(response.getArtistName()+" "+response.getMusicName())
                        .market(CountryCode.SE)
                        .limit(10)
                        .offset(0)
                        .build();
                Paging<Track> trackPaging = searchTracksRequest.execute();
                GetTrackRequest getTrackRequest = spotifyApi
                        .getTrack(trackPaging.getItems()[0]
                        .getId())
                        .build();
                Track track = getTrackRequest.execute();
                String url = track.getExternalUrls().get("spotify");
                String image = track.getAlbum().getImages()[0].getUrl();
                geminiKeySearchResponseWithSpotifies.add(new GeminiKeySearchResponseWithSpotify(response.getMusicName(), response.getArtistName(), image, url));
            }
            return geminiKeySearchResponseWithSpotifies;
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        } finally {
            return geminiKeySearchResponseWithSpotifies;
        }
    }

    @GetMapping("/keysearchrequest")
    public List<GeminiKeySearchResponseWithSpotify> keySearch(@RequestBody GeminiKeySearchRequest keySearchRequest) throws IOException {
        GeminiPromptByKeySearch promptByApp = new GeminiPromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());

        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ObjectMapper objectMapper = new ObjectMapper();
        try (VertexAI vertexAI = new VertexAI(PROJECT, LOCATION)) {
            GenerativeModel model = new GenerativeModel(MODEL, vertexAI);
            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    prompt.text()
            ));
            String responseVertexAi = ResponseHandler.getText(response);
            List<GeminiKeySearchResponse> geminiKeySearchResponses = objectMapper.readValue(responseVertexAi, new TypeReference<List<GeminiKeySearchResponse>>() {
            });
            List<GeminiKeySearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();
            return getKeySearchResponseWithSpotifies(geminiKeySearchResponses, responseWithSpotifies);
        }
    }
}


