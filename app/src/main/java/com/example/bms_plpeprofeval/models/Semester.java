package com.example.bms_plpeprofeval.models;

import java.io.Serializable;

public class Semester implements Serializable {
    private String semesterId;
    private int semesterNumber; // 1 or 2
    private int year;
    private String title; // e.g., "Fall 2024", "Spring 2025"
    private String startDate;
    private String endDate;
    private boolean isActive;
    private boolean isEvaluationOpen;

    public Semester() {
        isActive = true;
        isEvaluationOpen = false;
    }

    public Semester(String semesterId, int semesterNumber, int year) {
        this.semesterId = semesterId;
        this.semesterNumber = semesterNumber;
        this.year = year;
        this.title = (semesterNumber == 1 ? "Spring " : "Fall ") + year;
        isActive = true;
        isEvaluationOpen = false;
    }

    // Getters and Setters
    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isEvaluationOpen() {
        return isEvaluationOpen;
    }

    public void setEvaluationOpen(boolean evaluationOpen) {
        isEvaluationOpen = evaluationOpen;
    }

    @Override
    public String toString() {
        return title;
    }
}