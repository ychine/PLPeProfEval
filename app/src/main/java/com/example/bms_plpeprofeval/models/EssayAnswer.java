package com.example.bms_plpeprofeval.models;


public class EssayAnswer extends Response {
    private String answerText;
    private int wordCount;

    public EssayAnswer() {
        super();
    }

    public EssayAnswer(String answerId, String questionId, String evaluationId, String answerText) {
        super(answerId, questionId, evaluationId);
        this.answerText = answerText;
        this.wordCount = answerText != null ? countWords(answerText) : 0;
    }

    // Getters and Setters
    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
        this.wordCount = answerText != null ? countWords(answerText) : 0;
    }

    public int getWordCount() {
        return wordCount;
    }

    // Helper method to count words
    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        String[] words = text.trim().split("\\s+");
        return words.length;
    }
}