package ru.saratov.texttosql.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saratov.texttosql.dto.AnalyticsResponse;
import ru.saratov.texttosql.dto.QuestionRequest;
import ru.saratov.texttosql.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class QuestionController {

    private final AnalyticsService analyticsService;

    public QuestionController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AnalyticsResponse> ask(@RequestBody QuestionRequest request) {
        String answer = analyticsService.ask(request.getQuestion());
        return ResponseEntity.ok(new AnalyticsResponse(request.getQuestion(), answer));
    }
}