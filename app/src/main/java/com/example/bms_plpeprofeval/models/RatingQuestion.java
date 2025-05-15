package com.example.bms_plpeprofeval.models;

public class RatingQuestion extends Question {
    private int minRating; // Usually 1
    private int maxRating; // Usually 5
    private String minRatingLabel; // e.g., "Poor"
    private String maxRatingLabel; // e.g., "Excellent"
    private String category; // e.g., "Teaching Style", "Clarity", etc.

    public RatingQuestion() {
        super();
        setQuestionType("rating");
        this.minRating = 1;
        this.maxRating = 5;
        this.minRatingLabel = "Poor";
        this.maxRatingLabel = "Excellent";
    }

    public RatingQuestion(String questionId, String questionText, int orderIndex) {
        super(questionId, questionText, "rating", orderIndex);
        this.minRating = 1;
        this.maxRating = 5;
        this.minRatingLabel = "Poor";
        this.maxRatingLabel = "Excellent";
    }

    // Getters and Setters
    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public String getMinRatingLabel() {
        return minRatingLabel;
    }

    public void setMinRatingLabel(String minRatingLabel) {
        this.minRatingLabel = minRatingLabel;
    }

    public String getMaxRatingLabel() {
        return maxRatingLabel;
    }

    public void setMaxRatingLabel(String maxRatingLabel) {
        this.maxRatingLabel = maxRatingLabel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}