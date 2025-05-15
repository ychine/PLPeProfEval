package com.example.bms_plpeprofeval;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bms_plpeprofeval.DatabaseHelper;
import com.example.bms_plpeprofeval.R;
import com.example.bms_plpeprofeval.models.User;
import com.example.bms_plpeprofeval.utils.Constants;
import com.example.bms_plpeprofeval.utils.SessionManager;
import com.example.bms_plpeprofeval.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private ProgressBar progressBar;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper and session manager
        databaseHelper = DatabaseHelper.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            redirectBasedOnUserType();
            finish();
        }

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);
        progressBar = findViewById(R.id.progressBar);

        // Setup click listeners
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void loginUser() {
        // Get input values
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate inputs
        if (!ValidationUtils.isValidEmail(email)) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Attempt login
        User user = databaseHelper.getUserByCredentials(email, password);

        if (user != null) {
            // Create login session
            sessionManager.createLoginSession(user);

            // Redirect based on user type
            redirectBasedOnUserType();
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
        }

        // Hide progress bar
        progressBar.setVisibility(View.GONE);
    }

    private void redirectBasedOnUserType() {
        String userType = sessionManager.getUserType();
        Intent intent = null;

        switch (userType) {
            case Constants.USER_TYPE_STUDENT:
                intent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
                break;
            case Constants.USER_TYPE_PROFESSOR:
                intent = new Intent(LoginActivity.this, ProfessorDashboardActivity.class);
                break;
            case Constants.USER_TYPE_ADMIN:
                intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}