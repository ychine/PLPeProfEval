
package com.example.bms_plpeprofeval.models;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String studentId;
    private String department;
    private int yearLevel;
    private List<Course> enrolledCourses;
    private List<Evaluation> submittedEvaluations;

    public Student() {
        super();
        setUserType("student");
        enrolledCourses = new ArrayList<>();
        submittedEvaluations = new ArrayList<>();
    }

    public Student(String userId, String email, String password, String fullName, String studentId) {
        super(userId, email, password, fullName, "student");
        this.studentId = studentId;
        enrolledCourses = new ArrayList<>();
        submittedEvaluations = new ArrayList<>();
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void addCourse(Course course) {
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }
        enrolledCourses.add(course);
    }

    public List<Evaluation> getSubmittedEvaluations() {
        return submittedEvaluations;
    }

    public void setSubmittedEvaluations(List<Evaluation> submittedEvaluations) {
        this.submittedEvaluations = submittedEvaluations;
    }

    public void addEvaluation(Evaluation evaluation) {
        if (submittedEvaluations == null) {
            submittedEvaluations = new ArrayList<>();
        }
        submittedEvaluations.add(evaluation);
    }

    // Check if student has already evaluated a specific course
    public boolean hasEvaluatedCourse(String courseId, String semesterId) {
        if (submittedEvaluations == null) return false;

        for (Evaluation eval : submittedEvaluations) {
            if (eval.getCourseId().equals(courseId) && eval.getSemesterId().equals(semesterId)) {
                return true;
            }
        }
        return false;
    }
}