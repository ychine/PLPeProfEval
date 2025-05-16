package com.example.bms_plpeprofeval;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bms_plpeprofeval.models.Course;
import com.example.bms_plpeprofeval.models.Evaluation;
import com.example.bms_plpeprofeval.models.Professor;
import com.example.bms_plpeprofeval.models.Student;
import com.example.bms_plpeprofeval.utils.Constants;
import com.example.bms_plpeprofeval.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EvaluationActivity extends AppCompatActivity {

    private TextView textViewCourseName, textViewProfessorName;
    private RatingBar ratingBarTeaching, ratingBarCommunication, ratingBarPreparation, ratingBarKnowledge;
    private EditText editTextComments;
    private Button buttonSubmitEvaluation;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Student currentStudent;
    private Course currentCourse;
    private Professor currentProfessor;
    private String courseId;
    private String professorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Professor Evaluation");
        }

        // Initialize database helper and session manager
        databaseHelper = DatabaseHelper.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        // Get current user and convert to Student
        if (sessionManager.getUser() instanceof Student) {
            currentStudent = (Student) sessionManager.getUser();
        } else {
            // Redirect back to login if user is not a student
            sessionManager.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Get course and professor IDs from intent
        Intent intent = getIntent();
        if (intent != null) {
            courseId = intent.getStringExtra(Constants.EXTRA_COURSE_ID);
            professorId = intent.getStringExtra(Constants.EXTRA_PROFESSOR_ID);
        }

        // Validate received data
        if (courseId == null || professorId == null) {
            Toast.makeText(this, "Error: Missing course or professor information", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load course and professor data
        loadCourseAndProfessorData();

        // Initialize views
        initializeViews();

        // Set up listeners
        setupListeners();
    }

    private void loadCourseAndProfessorData() {
        try {
            // Load course data
            currentCourse = databaseHelper.getCourseById(courseId);
            if (currentCourse == null) {
                Toast.makeText(this, "Error: Course not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Load professor data
            currentProfessor = databaseHelper.getProfessorById(professorId);
            if (currentProfessor == null) {
                Toast.makeText(this, "Error: Professor not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        textViewCourseName = findViewById(R.id.textViewCourseName);
        textViewProfessorName = findViewById(R.id.textViewProfessorName);
        ratingBarTeaching = findViewById(R.id.ratingBarTeaching);
        ratingBarCommunication = findViewById(R.id.ratingBarCommunication);
        ratingBarPreparation = findViewById(R.id.ratingBarPreparation);
        ratingBarKnowledge = findViewById(R.id.ratingBarKnowledge);
        editTextComments = findViewById(R.id.editTextComments);
        buttonSubmitEvaluation = findViewById(R.id.buttonSubmitEvaluation);

        // Set course and professor names
        textViewCourseName.setText(currentCourse.getCourseName());
        textViewProfessorName.setText(currentProfessor.getFullName());
    }

    private void setupListeners() {
        buttonSubmitEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvaluation();
            }
        });
    }

    private void submitEvaluation() {
        try {
            // Get ratings
            float teachingRating = ratingBarTeaching.getRating();
            float communicationRating = ratingBarCommunication.getRating();
            float preparationRating = ratingBarPreparation.getRating();
            float knowledgeRating = ratingBarKnowledge.getRating();

            // Validate ratings
            if (teachingRating == 0 || communicationRating == 0 || preparationRating == 0 || knowledgeRating == 0) {
                Toast.makeText(this, "Please provide ratings for all categories", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get comments
            String comments = editTextComments.getText().toString().trim();

            // Create evaluation object
            Evaluation evaluation = new Evaluation();
            evaluation.setStudentId(currentStudent.getUserId());
            evaluation.setProfessorId(professorId);
            evaluation.setCourseId(courseId);
            evaluation.setTeachingRating(teachingRating);
            evaluation.setCommunicationRating(communicationRating);
            evaluation.setPreparationRating(preparationRating);
            evaluation.setKnowledgeRating(knowledgeRating);
            evaluation.setComments(comments);

            // Set current date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            evaluation.setEvaluationDate(sdf.format(new Date()));

            // Save evaluation to database
            long result = databaseHelper.addEvaluation(evaluation);

            if (result > 0) {
                Toast.makeText(this, "Evaluation submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to submit evaluation", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error submitting evaluation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle back button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}