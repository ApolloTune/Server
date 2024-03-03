package com.apollotune.server.openai.controllers;

import com.apollotune.server.openai.payloads.request.KeySearchRequest;
import com.apollotune.server.openai.prompt.PromptByKeySearch;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;


@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class ChatController {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String MODEL;

    @Value("${openai.api.url}")
    private String API_URL;

    @Value("${openai.api.key}")
    private String OPEN_API_KEY;

    @GetMapping("/keysearchrequest")
    public String keySearch(@RequestBody KeySearchRequest keySearchRequest) {
        PromptByKeySearch promptByApp = new PromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());

        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(OPEN_API_KEY)
                .modelName(MODEL)
                .temperature(0.3)
                .build();
        return model.generate(prompt.text());
    }
}
