package com.apollotune.server.openai.controllers;

import com.apollotune.server.openai.payloads.request.ChatRequest;
import com.apollotune.server.openai.payloads.request.KeySearchRequest;
import com.apollotune.server.openai.payloads.response.ChatResponse;
import com.apollotune.server.openai.prompt.PromptByApp;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import java.util.HashMap;
import java.util.Map;

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
    public String keySearch(@RequestBody KeySearchRequest keySearchRequest){
        /*
        ChatRequest request = new ChatRequest(MODEL, prompt);
        request.setN(1);
        ChatResponse response = restTemplate.postForObject(API_URL, request, ChatResponse.class);
        if(response == null || response.getChoices() == null || response.getChoices().isEmpty()){
            return "No response";
        }
        return response.getChoices().get(0).getMessage().getContent();
         */
        PromptTemplate promptTemplate = PromptTemplate
                .from(PromptByApp.KEY_SEARCH3);
        Map<String, Object> variables = new HashMap<>();
        keySearchRequest.getEmotions().forEach(musicEmotion -> variables.put("musicemotion", musicEmotion));
        keySearchRequest.getMusicYears().forEach(musicYear -> variables.put("musicyear", musicYear));
        keySearchRequest.getMusicTypes().forEach(musicType -> variables.put("musictype", musicType));
        keySearchRequest.getMusicLanguages().forEach(musicLanguages -> variables.put("musiclanguages", musicLanguages));
        Prompt prompt = promptTemplate.apply(variables);


        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(OPEN_API_KEY)
                .modelName(MODEL)
                .temperature(0.3)
                .build();
        return model.generate(prompt.text());
    }

}
