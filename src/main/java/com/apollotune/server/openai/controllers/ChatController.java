package com.apollotune.server.openai.controllers;

import com.apollotune.server.openai.payloads.request.KeySearchRequest;
import com.apollotune.server.openai.payloads.response.KeySearchResponse;
import com.apollotune.server.openai.prompt.PromptByKeySearch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class ChatController {

    @Value("${openai.model}")
    private String MODEL;

    @Value("${openai.api.url}")
    private String API_URL;

    @Value("${openai.api.key}")
    private String OPEN_API_KEY;

    @Value("${spotify.client-secret-id}")
    private String SPOTIFY_CLIENT_SECRET_ID;

    @Value("${spotify.client-id}")
    private String SPOTIFY_CLIENT_ID;

    @Value("${spotify.redirect-url}")
    private String SPOTIFY_REDIRECT_URL;



    /*
    @GetMapping("/keysearchrequest")
    public List<KeySearchResponse> keySearch(@RequestBody KeySearchRequest keySearchRequest) throws IOException {
        PromptByKeySearch promptByApp = new PromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());
        ObjectMapper objectMapper = new ObjectMapper();
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel.builder().apiKey(OPEN_API_KEY).modelName(MODEL).temperature(0.3).build();
        String responseGpt = model.generate(prompt.text());
        List<KeySearchResponse> keySearchResponses = objectMapper.readValue(responseGpt, new TypeReference<List<KeySearchResponse>>() {
        });
        return keySearchResponses;

    }

     */
    @GetMapping("/keysearchrequest")
    public String keySearch(@RequestBody KeySearchRequest keySearchRequest) throws IOException {
        PromptByKeySearch promptByApp = new PromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());
        ObjectMapper objectMapper = new ObjectMapper();
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel.builder().apiKey(OPEN_API_KEY).modelName(MODEL).temperature(0.3).build();
        String responseGpt = model.generate(prompt.text());
        List<KeySearchResponse> keySearchResponses = objectMapper.readValue(responseGpt, new TypeReference<List<KeySearchResponse>>() {
        });
        try{
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(SPOTIFY_CLIENT_ID)
                    .setClientSecret(SPOTIFY_CLIENT_SECRET_ID)
                    .build();
            ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                    .build();
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(keySearchResponses.get(0).getMusicName())
            .market(CountryCode.SE)
            .limit(10)
            .offset(0)
            .build();
            Paging<Track> trackPaging = searchTracksRequest.execute();
            System.out.println("Return : "+ trackPaging.getItems()[0]);
            //CompletableFuture<Paging<Track>> pagingFuture = searchTracksRequest.executeAsync();
            //Paging<Track> trackPaging1 = pagingFuture.join();
            return String.valueOf(trackPaging.getTotal());
        }catch (Exception e){
            return e.getMessage();

        }

    }
}
