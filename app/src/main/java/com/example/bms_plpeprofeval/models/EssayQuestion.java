package com.example.bms_plpeprofeval.models;

public class EssayQuestion extends Question {
    private int minWordCount;
    private int maxWordCount;
    private String placeholderText;

    public EssayQuestion() {
        super();
        setQuestionType("essay");
    }

    public EssayQuestion(String questionId, String questionText, int orderIndex) {
        super(questionId, questionText, "essay", orderIndex);
        this.minWordCount = 50;
        this.maxWordCount = 500;
    }

    // Getters and Setters
    public int getMinWordCount() {
        return minWordCount;
    }

    public void setMinWordCount(int minWordCount) {
        this.minWordCount = minWordCount;
    }

    public int getMaxWordCount() {
        return maxWordCount;
    }

    public void setMaxWordCount(int maxWordCount) {
        this.maxWordCount = maxWordCount;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
    }
}