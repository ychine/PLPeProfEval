package com.example.bms_plpeprofeval.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bms_plpeprofeval.models.Admin;
import com.example.bms_plpeprofeval.models.Professor;
import com.example.bms_plpeprofeval.models.Student;
import com.example.bms_plpeprofeval.models.User;

public class SessionManager {
    private static final String PREF_NAME = "BMSProfEvalPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_DEPARTMENT = "department";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // Singleton instance
    private static SessionManager instance;

    // Constructor
    private SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Get singleton instance
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    // Create login session
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_TYPE, user.getUserType());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NAME, user.getFullName());
        editor.putString(KEY_DEPARTMENT, user.get());
        editor.commit();
    }

    // Get user details
    public User getUser() {
        if (!this.isLoggedIn()) {
            return null;
        }

        String userId = pref.getString(KEY_USER_ID, "");
        String userType = pref.getString(KEY_USER_TYPE, "");
        String email = pref.getString(KEY_EMAIL, "");
        String name = pref.getString(KEY_NAME, "");
        String department = pref.getString(KEY_DEPARTMENT, "");

        // Create appropriate user type
        switch (userType) {
            case Constants.USER_TYPE_STUDENT:
                return new Student(userId, email, "", name, department);
            case Constants.USER_TYPE_PROFESSOR:
                return new Professor(userId, email, "", name, department);
            case Constants.USER_TYPE_ADMIN:
                return new Admin(userId, email, "", name, department);
            default:
                return null;
        }
    }

    // Check login status
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Logout user
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    // Get user type
    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, "");
    }
}