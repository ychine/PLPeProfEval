package com.example.bms_plpeprofeval.models;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private String professorId;
    private String department;
    private String title; // e.g., "Assistant Professor", "Associate Professor", etc.
    private List<Course> taughtCourses;
    private List<Evaluation> receivedEvaluations; // Only accessible data, not student identities

    public Professor() {
        super();
        setUserType("professor");
        taughtCourses = new ArrayList<>();
        receivedEvaluations = new ArrayList<>();
    }

    public Professor(String userId, String email, String password, String fullName, String professorId) {
        super(userId, email, password, fullName, "professor");
        this.professorId = professorId;
        taughtCourses = new ArrayList<>();
        receivedEvaluations = new ArrayList<>();
    }

    // Getters and Setters
    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Course> getTaughtCourses() {
        return taughtCourses;
    }

    public void setTaughtCourses(List<Course> taughtCourses) {
        this.taughtCourses = taughtCourses;
    }

    public void addCourse(Course course) {
        if (taughtCourses == null) {
            taughtCourses = new ArrayList<>();
        }
        taughtCourses.add(course);
    }

    public List<Evaluation> getReceivedEvaluations() {
        return receivedEvaluations;
    }

    public void setReceivedEvaluations(List<Evaluation> receivedEvaluations) {
        this.receivedEvaluations = receivedEvaluations;
    }

    public void addEvaluation(Evaluation evaluation) {
        if (receivedEvaluations == null) {
            receivedEvaluations = new ArrayList<>();
        }
        // Make sure to clear any student-identifying information
        evaluation.setStudentId("anonymous");
        receivedEvaluations.add(evaluation);
    }

    // Calculate average rating across all evaluations
    public double getAverageRating() {
        if (receivedEvaluations == null || receivedEvaluations.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0.0;
        int count = 0;

        for (Evaluation eval : receivedEvaluations) {
            for (RatingAnswer answer : eval.getRatingAnswers()) {
                totalRating += answer.getRatingValue();
                count++;
            }
        }

        return count > 0 ? totalRating / count : 0.0;
    }

    // Get average rating by question
    public double getAverageRatingByQuestion(String questionId) {
        if (receivedEvaluations == null || receivedEvaluations.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0.0;
        int count = 0;

        for (Evaluation eval : receivedEvaluations) {
            for (RatingAnswer answer : eval.getRatingAnswers()) {
                if (answer.getQuestionId().equals(questionId)) {
                    totalRating += answer.getRatingValue();
                    count++;
                }
            }
        }

        return count > 0 ? totalRating / count : 0.0;
    }
}