package ru.saratov.texttosql.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saratov.texttosql.dto.ChatRequest;
import ru.saratov.texttosql.dto.ChatResponse;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/generate")
    public ResponseEntity<ChatResponse> generate(@RequestBody ChatRequest request) {
        String answer = chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
        return ResponseEntity.ok(new ChatResponse(request.getMessage(), answer));
    }
}