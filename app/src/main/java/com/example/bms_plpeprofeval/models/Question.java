package com.example.bms_plpeprofeval.models;

import java.io.Serializable;

public abstract class Question implements Serializable {
    private String questionId;
    private String questionText;
    private String questionType; // "essay" or "rating"
    private int orderIndex; // to maintain question order
    private boolean isRequired;
    private boolean isActive;

    public Question() {
        isRequired = true;
        isActive = true;
    }

    public Question(String questionId, String questionText, String questionType, int orderIndex) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.orderIndex = orderIndex;
        this.isRequired = true;
        this.isActive = true;
    }

    // Getters and Setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}