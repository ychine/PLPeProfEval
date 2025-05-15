package com.example.bms_plpeprofeval.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Evaluation implements Serializable {
    private String evaluationId;
    private String courseId;
    private String professorId;
    private String studentId; // To track who submitted, but hidden from professor view
    private String semesterId;
    private String submissionDate;
    private List<EssayAnswer> essayAnswers;
    private List<RatingAnswer> ratingAnswers;
    private String additionalComments; // Optional overall comments
    private boolean isAnonymized; // Whether student info has been removed for professor viewing

    public Evaluation() {
        essayAnswers = new ArrayList<>();
        ratingAnswers = new ArrayList<>();
        isAnonymized = false;
    }

    public Evaluation(String evaluationId, String courseId, String professorId, String studentId, String semesterId) {
        this.evaluationId = evaluationId;
        this.courseId = courseId;
        this.professorId = professorId;
        this.studentId = studentId;
        this.semesterId = semesterId;
        essayAnswers = new ArrayList<>();
        ratingAnswers = new ArrayList<>();
        isAnonymized = false;
    }

    // Getters and Setters
    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public List<EssayAnswer> getEssayAnswers() {
        return essayAnswers;
    }

    public void setEssayAnswers(List<EssayAnswer> essayAnswers) {
        this.essayAnswers = essayAnswers;
    }

    public void addEssayAnswer(EssayAnswer answer) {
        if (essayAnswers == null) {
            essayAnswers = new ArrayList<>();
        }
        essayAnswers.add(answer);
    }

    public List<RatingAnswer> getRatingAnswers() {
        return ratingAnswers;
    }

    public void setRatingAnswers(List<RatingAnswer> ratingAnswers) {
        this.ratingAnswers = ratingAnswers;
    }

    public void addRatingAnswer(RatingAnswer answer) {
        if (ratingAnswers == null) {
            ratingAnswers = new ArrayList<>();
        }
        ratingAnswers.add(answer);
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public boolean isAnonymized() {
        return isAnonymized;
    }

    public void setAnonymized(boolean anonymized) {
        isAnonymized = anonymized;
    }

    // Create an anonymized copy for professor viewing
    public Evaluation createAnonymizedCopy() {
        Evaluation anonymized = new Evaluation();
        anonymized.setEvaluationId(this.evaluationId);
        anonymized.setCourseId(this.courseId);
        anonymized.setProfessorId(this.professorId);
        anonymized.setStudentId("anonymous"); // Hide student ID
        anonymized.setSemesterId(this.semesterId);
        anonymized.setSubmissionDate(this.submissionDate);
        anonymized.setEssayAnswers(this.essayAnswers);
        anonymized.setRatingAnswers(this.ratingAnswers);
        anonymized.setAdditionalComments(this.additionalComments);
        anonymized.setAnonymized(true);

        return anonymized;
    }

    // Calculate average rating for this evaluation
    public double getAverageRating() {
        if (ratingAnswers == null || ratingAnswers.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (RatingAnswer answer : ratingAnswers) {
            total += answer.getRatingValue();
        }

        return total / ratingAnswers.size();
    }
}