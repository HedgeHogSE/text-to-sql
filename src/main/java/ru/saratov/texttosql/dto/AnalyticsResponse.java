package ru.saratov.texttosql.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AnalyticsResponse {

    private String question;
    private String answer;

    public AnalyticsResponse(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}