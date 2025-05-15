package com.example.bms_plpeprofeval.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private String courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private String department;
    private int creditHours;
    private List<Professor> professors;
    private List<String> semesterOffered; // List of semester IDs when this course is offered
    private boolean isActive;

    public Course() {
        professors = new ArrayList<>();
        semesterOffered = new ArrayList<>();
        isActive = true;
    }

    public Course(String courseId, String courseCode, String courseName) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        professors = new ArrayList<>();
        semesterOffered = new ArrayList<>();
        isActive = true;
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public void addProfessor(Professor professor) {
        if (professors == null) {
            professors = new ArrayList<>();
        }
        professors.add(professor);
    }

    public List<String> getSemesterOffered() {
        return semesterOffered;
    }

    public void setSemesterOffered(List<String> semesterOffered) {
        this.semesterOffered = semesterOffered;
    }

    public void addSemester(String semesterId) {
        if (semesterOffered == null) {
            semesterOffered = new ArrayList<>();
        }
        semesterOffered.add(semesterId);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}