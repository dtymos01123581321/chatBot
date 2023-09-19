package com.undercurrent.backendchatbot.service;

import com.undercurrent.backendchatbot.entity.responseEntity.OpenAiRequest;
import com.undercurrent.backendchatbot.entity.responseEntity.OpenAiResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {
    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final String AUTH_KEY = "sk-API_KEY";

    public OpenAiResponse fetchCompletion(String msg) {
        RestTemplate restTemplate = new RestTemplate();
        OpenAiRequest request = new OpenAiRequest();
        request.addMessage("user", msg);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + AUTH_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, OpenAiResponse.class);
        System.out.println("msg  --: 4  " + response.getBody());

        return response.getBody();
    }
}

