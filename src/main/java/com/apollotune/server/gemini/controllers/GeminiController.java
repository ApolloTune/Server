package com.apollotune.server.gemini.controllers;


import com.apollotune.server.gemini.payloads.request.GeminiKeySearchRequest;
import com.apollotune.server.gemini.prompt.GeminiPromptByKeySearch;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    public String keySearch(@RequestBody GeminiKeySearchRequest keySearchRequest) throws IOException {
        GeminiPromptByKeySearch promptByApp = new GeminiPromptByKeySearch();

        promptByApp.setMusicemotion(keySearchRequest.getEmotions());
        promptByApp.setMusicyear(keySearchRequest.getMusicYears());
        promptByApp.setMusictype(keySearchRequest.getMusicTypes());
        promptByApp.setMusiclanguages(keySearchRequest.getMusicLanguages());

        Prompt prompt = StructuredPromptProcessor.toPrompt(promptByApp);

        try (VertexAI vertexAI = new VertexAI(PROJECT, LOCATION)) {


            GenerativeModel model = new GenerativeModel(MODEL, vertexAI);
            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    prompt.text()
            ));
            return response.toString();
        }
    }
}

