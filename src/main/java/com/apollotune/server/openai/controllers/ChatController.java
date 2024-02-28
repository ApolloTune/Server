package com.apollotune.server.openai.controllers;

import com.apollotune.server.openai.payloads.request.ChatRequest;
import com.apollotune.server.openai.payloads.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String MODEL;

    @Value("${openai.api.url}")
    private String API_URL;

    @GetMapping("/chatgpt")
    public String chat(@RequestParam String prompt){
        ChatRequest request = new ChatRequest(MODEL, prompt);
        request.setN(1);
        ChatResponse response = restTemplate.postForObject(API_URL, request, ChatResponse.class);
        if(response == null || response.getChoices() == null || response.getChoices().isEmpty()){
            return "No response";
        }
        return response.getChoices().get(0).getMessage().getContent();
    }

}
