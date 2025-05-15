package com.example.bms_plpeprofeval.models;

public class RatingAnswer extends Response {
    private int ratingValue;
    private String comment;

    public RatingAnswer() {
        super();
    }

    public RatingAnswer(String answerId, String questionId, String evaluationId, int ratingValue) {
        super(answerId, questionId, evaluationId);
        this.ratingValue = ratingValue;
    }

    // Getters and Setters
    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}