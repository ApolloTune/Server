package com.apollotune.server.openai.controllers;

import com.apollotune.server.openai.payloads.request.KeySearchRequest;
import com.apollotune.server.openai.payloads.response.KeySearchResponse;
import com.apollotune.server.openai.prompt.PromptByKeySearch;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.List;


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

    @GetMapping("/keysearchrequest")
    public List<KeySearchResponse> keySearch(@RequestBody KeySearchRequest keySearchRequest) throws IOException {
        PromptByKeySearch promptByApp = new PromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());
        ObjectMapper objectMapper = new ObjectMapper();
        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(OPEN_API_KEY)
                .modelName(MODEL)
                .temperature(0.3)
                .build();
        String responseGpt = model.generate(prompt.text());
        List<KeySearchResponse> keySearchResponses = objectMapper.readValue(responseGpt, new TypeReference<List<KeySearchResponse>>() {});
        return keySearchResponses;

    }
}
