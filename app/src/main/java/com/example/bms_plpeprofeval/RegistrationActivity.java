package com.example.bms_plpeprofeval;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bms_plpeprofeval.models.Professor;
import com.example.bms_plpeprofeval.models.Student;
import com.example.bms_plpeprofeval.models.User;
import com.example.bms_plpeprofeval.utils.Constants;
import com.example.bms_plpeprofeval.utils.ValidationUtils;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName, editTextDepartment;
    private Spinner spinnerUserType;
    private Button buttonRegister;
    private ProgressBar progressBar;

    private DatabaseHelper databaseHelper;
    private String selectedUserType = Constants.USER_TYPE_STUDENT; // Default to student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        spinnerUserType = findViewById(R.id.spinnerUserType);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);

        // Setup user type spinner
        setupUserTypeSpinner();

        // Setup register button click
        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void setupUserTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);

        // Add user types (excluding admin)
        adapter.add("Student");
        adapter.add("Professor");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);
        spinnerUserType.setSelection(0); // Default selection

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString().toLowerCase();
                if (selected.equals("student")) {
                    selectedUserType = Constants.USER_TYPE_STUDENT;
                } else if (selected.equals("professor")) {
                    selectedUserType = Constants.USER_TYPE_PROFESSOR;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUserType = Constants.USER_TYPE_STUDENT;
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        // Input validation
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

        if (!ValidationUtils.isValidName(name)) {
            editTextName.setError("Please enter your name");
            editTextName.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidDepartment(department)) {
            editTextDepartment.setError("Please enter your department");
            editTextDepartment.requestFocus();
            return;
        }

        if (databaseHelper.isEmailExists(email)) {
            editTextEmail.setError("Email already registered");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Create user object
        String userId = UUID.randomUUID().toString();
        User user = selectedUserType.equals(Constants.USER_TYPE_STUDENT)
                ? new Student(userId, email, password, name, department)
                : new Professor(userId, email, password, name, department);

        // Save to database
        long result = databaseHelper.addUser(user);
        progressBar.setVisibility(View.GONE);

        if (result != -1) {
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
