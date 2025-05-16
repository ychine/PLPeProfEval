package com.example.bms_plpeprofeval;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bms_plpeprofeval.models.Course;
import com.example.bms_plpeprofeval.models.Student;
import com.example.bms_plpeprofeval.models.User;
import com.example.bms_plpeprofeval.utils.Constants;
import com.example.bms_plpeprofeval.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {

    private TextView textViewWelcome, textViewNoCoursesMessage;
    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Student currentStudent;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Initialize database helper and session manager
        databaseHelper = DatabaseHelper.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        // Get current user and convert to Student
        User user = sessionManager.getUser();
        if (user instanceof Student) {
            currentStudent = (Student) user;
        } else {
            // Redirect back to login if user is not a student
            sessionManager.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize views
        textViewWelcome = findViewById(R.id.textViewWelcome);
        textViewNoCoursesMessage = findViewById(R.id.textViewNoCoursesMessage);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);

        // Setup welcome message
        String welcomeMessage = "Welcome, " + currentStudent.getFullName();
        textViewWelcome.setText(welcomeMessage);

        // Setup recycler view
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        // Initialize courses list
        courses = new ArrayList<>();

        // Load courses
        loadCourses();
    }

    private void loadCourses() {
        try {
            // Get all courses (in a real app, this would be filtered by student's enrolled courses)
            courses = databaseHelper.getAllCourses();

            if (courses == null) {
                courses = new ArrayList<>();
            }

            if (courses.isEmpty()) {
                textViewNoCoursesMessage.setVisibility(View.VISIBLE);
                recyclerViewCourses.setVisibility(View.GONE);
            } else {
                textViewNoCoursesMessage.setVisibility(View.GONE);
                recyclerViewCourses.setVisibility(View.VISIBLE);

                // Setup adapter
                courseAdapter = new CourseAdapter(this, courses, this);
                recyclerViewCourses.setAdapter(courseAdapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading courses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            textViewNoCoursesMessage.setVisibility(View.VISIBLE);
            recyclerViewCourses.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCourseClick(Course course) {
        if (course == null || currentStudent == null) {
            Toast.makeText(this, "Error: Course or student data is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Check if the student has already evaluated this course's professor
            String studentId = currentStudent.getUserId();
            String professorId = course.getProfessorId();
            String courseId = course.getCourseId();

            boolean hasEvaluated = databaseHelper.hasStudentEvaluated(studentId, professorId, courseId);

            if (hasEvaluated) {
                // Student has already evaluated this course/professor
                Toast.makeText(this, "You have already evaluated this course", Toast.LENGTH_SHORT).show();
                return;
            }

            // Start evaluation activity
            Intent intent = new Intent(this, EvaluationActivity.class);
            intent.putExtra(Constants.EXTRA_COURSE_ID, courseId);
            intent.putExtra(Constants.EXTRA_PROFESSOR_ID, professorId);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error starting evaluation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            sessionManager.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }
}