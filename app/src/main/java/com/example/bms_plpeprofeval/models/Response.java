package com.example.bms_plpeprofeval.models;

public abstract class Response {
    private String responseId;
    private String evaluationId;
    private String questionId;

    public Response(String responseId, String evaluationId, String questionId) {
        this.responseId = responseId;
        this.evaluationId = evaluationId;
        this.questionId = questionId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}

// Concrete implementation for essay responses
class EssayResponse extends Response {
    private String response;

    public EssayResponse(String responseId, String evaluationId, String questionId, String response) {
        super(responseId, evaluationId, questionId);
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

// Concrete implementation for rating responses
class RatingResponse extends Response {
    private int rating;

    public RatingResponse(String responseId, String evaluationId, String questionId, int rating) {
        super(responseId, evaluationId, questionId);
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}