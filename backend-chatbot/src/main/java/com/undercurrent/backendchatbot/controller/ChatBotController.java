package com.undercurrent.backendchatbot.controller;

import com.undercurrent.backendchatbot.entity.responseEntity.OpenAiResponse;
import com.undercurrent.backendchatbot.entity.responseEntity.ResponseMessage;
import com.undercurrent.backendchatbot.entity.responseEntity.RequestMessage;
import com.undercurrent.backendchatbot.service.ChatBotService;
import com.undercurrent.backendchatbot.service.OpenAIService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class ChatBotController {

    private final ChatBotService chatBotService;
    private final OpenAIService openAiService;

    @Autowired
    public ChatBotController(ChatBotService chatBotService,
                             OpenAIService openAiService) {
        this.chatBotService = chatBotService;
        this.openAiService = openAiService;
    }

    private List<String> queries;

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

    @PostMapping("/openAI")
    public ResponseEntity<OpenAiResponse> sendAIMessage(@RequestBody RequestMessage msg) {
        OpenAiResponse responseMessage = openAiService.fetchCompletion(msg.getMsg());

        return ResponseEntity.ok(responseMessage);
    }
}
