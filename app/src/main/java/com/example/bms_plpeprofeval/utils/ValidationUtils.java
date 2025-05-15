package com.example.bms_plpeprofeval.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    // Validate email
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validate password (minimum 6 characters)
    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    // Validate name (not empty)
    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name);
    }

    // Validate department (not empty)
    public static boolean isValidDepartment(String department) {
        return !TextUtils.isEmpty(department);
    }

    // Validate course code (not empty)
    public static boolean isValidCourseCode(String courseCode) {
        return !TextUtils.isEmpty(courseCode);
    }

    // Validate course name (not empty)
    public static boolean isValidCourseName(String courseName) {
        return !TextUtils.isEmpty(courseName);
    }

    // Validate question text (not empty)
    public static boolean isValidQuestionText(String questionText) {
        return !TextUtils.isEmpty(questionText);
    }

    // Validate rating (between 1-5)
    public static boolean isValidRating(int rating) {
        return rating >= Constants.MIN_RATING && rating <= Constants.MAX_RATING;
    }
}