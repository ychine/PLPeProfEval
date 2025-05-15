package com.example.bms_plpeprofeval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bms_plpeprofeval.R;
import com.example.bms_plpeprofeval.utils.Constants;
import com.example.bms_plpeprofeval.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler to delay loading the next activity (splash screen)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, SPLASH_DISPLAY_TIME);
    }

    private void checkLoginStatus() {
        // Initialize session manager
        SessionManager sessionManager = SessionManager.getInstance(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Redirect based on user type
            String userType = sessionManager.getUserType();
            Intent intent = null;

            switch (userType) {
                case Constants.USER_TYPE_STUDENT:
                    intent = new Intent(MainActivity.this, StudentDashboardActivity.class);
                    break;
                case Constants.USER_TYPE_PROFESSOR:
                    intent = new Intent(MainActivity.this, ProfessorDashboardActivity.class);
                    break;
                case Constants.USER_TYPE_ADMIN:
                    intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }
        } else {
            // User is not logged in, go to login screen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        // Close the activity
        finish();
    }
}