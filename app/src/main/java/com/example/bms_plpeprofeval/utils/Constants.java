package com.example.bms_plpeprofeval.utils;

public class Constants {
    // User types
    public static final String USER_TYPE_STUDENT = "student";
    public static final String USER_TYPE_PROFESSOR = "professor";
    public static final String USER_TYPE_ADMIN = "admin";

    // Intent extras
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_COURSE_ID = "extra_course_id";
    public static final String EXTRA_PROFESSOR_ID = "extra_professor_id";
    public static final String EXTRA_EVALUATION_ID = "extra_evaluation_id";

    // Rating scale
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    // Academic years
    public static final String[] ACADEMIC_YEARS = {
            "2023-2024",
            "2024-2025",
            "2025-2026"
    };

    // Semesters
    public static final Integer[] SEMESTERS = {1, 2};
}