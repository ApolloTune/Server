package com.apollotune.server.openai.controllers;

import com.apollotune.server.exceptions.ApiException;
import com.apollotune.server.openai.payloads.request.FavoriteSearchRequest;
import com.apollotune.server.openai.payloads.request.KeySearchRequest;
import com.apollotune.server.openai.payloads.request.SentenceSearchRequest;
import com.apollotune.server.openai.payloads.response.KeySearchResponse;
import com.apollotune.server.openai.payloads.response.KeySearchResponseWithSpotify;
import com.apollotune.server.openai.prompt.PromptByFavoriteSearch;
import com.apollotune.server.openai.prompt.PromptByKeySearch;
import com.apollotune.server.openai.prompt.PromptBySentenceSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
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
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class ChatController {

    @Value("${openai.model}")
    private String MODEL;

    @Value("${openai.api.key}")
    private String OPEN_API_KEY;

    @Value("${spotify.client-secret-id}")
    private String SPOTIFY_CLIENT_SECRET_ID;

    @Value("${spotify.client-id}")
    private String SPOTIFY_CLIENT_ID;

    @Nullable
    private List<KeySearchResponseWithSpotify> getKeySearchResponseWithSpotifies(List<KeySearchResponse> keySearchResponses, List<KeySearchResponseWithSpotify> responseWithSpotifies) {
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

            for (KeySearchResponse response : keySearchResponses) {
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
                responseWithSpotifies.add(new KeySearchResponseWithSpotify(response.getMusicName(), response.getArtistName(), image, url));
            }
            return responseWithSpotifies;
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        } finally {
            return responseWithSpotifies;
        }
    }

    @GetMapping("/keysearchrequest")
    public List<KeySearchResponseWithSpotify> keySearch(@RequestBody KeySearchRequest keySearchRequest) throws IOException {
        PromptByKeySearch promptByApp = new PromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());
        ObjectMapper objectMapper = new ObjectMapper();
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey(OPEN_API_KEY)
                .modelName(MODEL)
                .temperature(0.3)
                .build();
        String responseGpt = model.generate(prompt.text());
        List<KeySearchResponse> keySearchResponses = objectMapper.readValue(responseGpt, new TypeReference<List<KeySearchResponse>>() {
        });
        List<KeySearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();

        return getKeySearchResponseWithSpotifies(keySearchResponses, responseWithSpotifies);
    }
    @GetMapping("/favoritesearchrequest")
    public List<KeySearchResponseWithSpotify> favoriteSearch(@RequestBody FavoriteSearchRequest favoriteSearchRequest) throws IOException {
        PromptByFavoriteSearch promptByApp = new PromptByFavoriteSearch();
        promptByApp.setSongname(favoriteSearchRequest.getMusicName());
        promptByApp.setSongartist(favoriteSearchRequest.getArtistName());
        ObjectMapper objectMapper = new ObjectMapper();
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey(OPEN_API_KEY)
                .modelName(MODEL)
                .temperature(0.3)
                .build();
        String responseGpt = model.generate(prompt.text());
        List<KeySearchResponse> keySearchResponses = objectMapper.readValue(responseGpt, new TypeReference<List<KeySearchResponse>>() {
        });
        List<KeySearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();

        return getKeySearchResponseWithSpotifies(keySearchResponses, responseWithSpotifies);
    }
    @GetMapping("/sentencesearchrequest")
    public List<KeySearchResponseWithSpotify> sentenceSearch(@RequestBody SentenceSearchRequest sentenceSearchRequest) throws JsonProcessingException {
        PromptBySentenceSearch promptByApp = new PromptBySentenceSearch();
        promptByApp.setSentence(sentenceSearchRequest.getSentence());
        ObjectMapper objectMapper = new ObjectMapper();
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey(OPEN_API_KEY)
                .modelName(MODEL)
                .temperature(0.3)
                .build();
        String responseGpt = model.generate(prompt.text());
        List<KeySearchResponse> keySearchResponses = objectMapper.readValue(responseGpt, new TypeReference<List<KeySearchResponse>>() {
        });
        List<KeySearchResponseWithSpotify> responseWithSpotifies = new ArrayList<>();
        return getKeySearchResponseWithSpotifies(keySearchResponses, responseWithSpotifies);
    }

}
