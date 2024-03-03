package com.apollotune.server.gemini.controllers;


import com.apollotune.server.gemini.payloads.request.GeminiKeySearchRequest;
import com.apollotune.server.gemini.prompt.GeminiPromptByKeySearch;
import com.apollotune.server.openai.payloads.request.KeySearchRequest;
import com.apollotune.server.openai.prompt.PromptByKeySearch;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.vertexai.VertexAiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/keysearchrequest")
    public String keySearch(@RequestBody GeminiKeySearchRequest keySearchRequest) {
        GeminiPromptByKeySearch promptByApp = new GeminiPromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());

        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);
        ChatLanguageModel model = VertexAiChatModel.builder()
                .project(PROJECT)
                .location(LOCATION)
                .modelName(MODEL)
                .build();
        return model.generate(prompt.text());
    }
}

