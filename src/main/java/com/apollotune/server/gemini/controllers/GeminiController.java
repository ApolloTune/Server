package com.apollotune.server.gemini.controllers;


import com.apollotune.server.exceptions.ApiException;
import com.apollotune.server.gemini.payloads.request.GeminiFavoriteSearchRequest;
import com.apollotune.server.gemini.payloads.request.GeminiKeySearchRequest;
import com.apollotune.server.gemini.payloads.request.GeminiSentenceSearchRequest;
import com.apollotune.server.gemini.payloads.response.GeminiSearchResponse;
import com.apollotune.server.gemini.payloads.response.GeminiSearchResponseWithSpotify;
import com.apollotune.server.gemini.prompt.GeminiPromptByFavoriteSearch;
import com.apollotune.server.gemini.prompt.GeminiPromptByKeySearch;
import com.apollotune.server.gemini.prompt.GeminiPromptBySentenceSearch;
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
    private List<GeminiSearchResponseWithSpotify> getSearchResponseWithSpotifies(List<GeminiSearchResponse> geminiKeySearchResponses, List<GeminiSearchResponseWithSpotify> geminiKeySearchResponseWithSpotifies) {
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

            for (GeminiSearchResponse response : geminiKeySearchResponses) {
                SearchTracksRequest searchTracksRequest = spotifyApi
                        .searchTracks(response.getArtistName() + " " + response.getMusicName())
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
                geminiKeySearchResponseWithSpotifies.add(new GeminiSearchResponseWithSpotify(response.getMusicName(), response.getArtistName(), image, url));
            }
            return geminiKeySearchResponseWithSpotifies;
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        } finally {
            return geminiKeySearchResponseWithSpotifies;
        }
    }

    @GetMapping("/keysearchrequest")
    public List<GeminiSearchResponseWithSpotify> keySearch(@RequestBody GeminiKeySearchRequest keySearchRequest) throws IOException {
        GeminiPromptByKeySearch promptByApp = new GeminiPromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());

        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ObjectMapper objectMapper = new ObjectMapper();
        VertexAI vertexAI = new VertexAI(PROJECT, LOCATION);
        GenerativeModel model = new GenerativeModel(MODEL, vertexAI);
        GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                prompt.text()
        ));
        String responseVertexAi = ResponseHandler.getText(response);
        List<GeminiSearchResponse> geminiKeySearchResponses = objectMapper.readValue(responseVertexAi, new TypeReference<List<GeminiSearchResponse>>() {
        });
        List<GeminiSearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();
        return getSearchResponseWithSpotifies(geminiKeySearchResponses, responseWithSpotifies);

    }

    @GetMapping("/favoritesearchrequest")
    public List<GeminiSearchResponseWithSpotify> favoriteSearch(@RequestBody GeminiFavoriteSearchRequest favoriteSearchRequest) throws IOException {
        GeminiPromptByFavoriteSearch promptByApp = new GeminiPromptByFavoriteSearch();
        promptByApp.setSongname(favoriteSearchRequest.getMusicName());
        promptByApp.setSongartist(favoriteSearchRequest.getArtistName());
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ObjectMapper objectMapper = new ObjectMapper();
        VertexAI vertexAI = new VertexAI(PROJECT, LOCATION);
        GenerativeModel model = new GenerativeModel(MODEL, vertexAI);
        GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                prompt.text()
        ));
        String responseVertexAi = ResponseHandler.getText(response);
        List<GeminiSearchResponse> geminiKeySearchResponses = objectMapper.readValue(responseVertexAi, new TypeReference<List<GeminiSearchResponse>>() {
        });
        List<GeminiSearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();
        return getSearchResponseWithSpotifies(geminiKeySearchResponses, responseWithSpotifies);
    }

    @GetMapping("/sentencesearchrequest")
    public List<GeminiSearchResponseWithSpotify> sentenceSearch(@RequestBody GeminiSentenceSearchRequest sentenceSearchRequest) throws IOException {
        GeminiPromptBySentenceSearch promptByApp = new GeminiPromptBySentenceSearch();
        promptByApp.setSentence(sentenceSearchRequest.getSentence());
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);

        ObjectMapper objectMapper = new ObjectMapper();
        try (VertexAI vertexAI = new VertexAI(PROJECT, LOCATION)) {
            GenerativeModel model = new GenerativeModel(MODEL, vertexAI);
            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    prompt.text()
            ));
            String responseVertexAi = ResponseHandler.getText(response);
            List<GeminiSearchResponse> geminiKeySearchResponses = objectMapper.readValue(responseVertexAi, new TypeReference<List<GeminiSearchResponse>>() {
            });
            List<GeminiSearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();
            return getSearchResponseWithSpotifies(geminiKeySearchResponses, responseWithSpotifies);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            VertexAI vertexAI = new VertexAI(PROJECT, LOCATION);
            GenerativeModel model = new GenerativeModel(MODEL, vertexAI);
            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    "Suggest the 10 most listened songs"
            ));
            String responseVertexAi = ResponseHandler.getText(response);
            List<GeminiSearchResponse> geminiKeySearchResponses = objectMapper.readValue(responseVertexAi, new TypeReference<List<GeminiSearchResponse>>() {
            });
            List<GeminiSearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();
            return getSearchResponseWithSpotifies(geminiKeySearchResponses, responseWithSpotifies);
        }
    }
}


