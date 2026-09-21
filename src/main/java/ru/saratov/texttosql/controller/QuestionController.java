package ru.saratov.texttosql.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saratov.texttosql.agent.AnalyticsAgent;
import ru.saratov.texttosql.dto.AnalyticsResponse;
import ru.saratov.texttosql.dto.QuestionRequest;

@RestController
@RequestMapping("/api/analytics")
public class QuestionController {

    private final AnalyticsAgent analyticsAgent;

    public QuestionController(AnalyticsAgent analyticsAgent) {
        this.analyticsAgent = analyticsAgent;
    }

    @PostMapping("/ask")
    public ResponseEntity<AnalyticsResponse> ask(@RequestBody QuestionRequest request) {
        String answer = analyticsAgent.answerQuestion(request.getQuestion());
        return ResponseEntity.ok(new AnalyticsResponse(request.getQuestion(), answer));
    }
}