package com.undercurrent.backendchatbot.controller;

import com.undercurrent.backendchatbot.entity.responseEntity.ResponseMessage;
import com.undercurrent.backendchatbot.entity.responseEntity.RequestMessage;
import com.undercurrent.backendchatbot.service.ChatBotService;
import com.undercurrent.backendchatbot.service.openAiApi.dto.ChatGPTRequest;
import com.undercurrent.backendchatbot.service.openAiApi.dto.ChatGPTResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin("http://localhost:3000")
public class ChatBotController {

    private final ChatBotService chatBotService;
    private final RestTemplate openaiRestTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private List<String> queries;

    @Autowired
    public ChatBotController(ChatBotService chatBotService,
                             @Qualifier("openaiRestTemplate") RestTemplate restTemplate) {
        this.chatBotService = chatBotService;
        this.openaiRestTemplate = restTemplate;
    }

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(new ClassPathResource("queries.txt").getURI());
        queries = Files.readAllLines(path);
    }

    @PostMapping("/message")
    public ResponseEntity<ResponseMessage> sendMessage(@RequestBody RequestMessage msg) {
        ResponseMessage responseMessage = chatBotService.getResponseMessage(msg, queries);

        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/gpt")
    public String chatGPT(@RequestParam("prompt") String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse = openaiRestTemplate.postForObject(apiUrl, request, ChatGPTResponse.class);

        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
