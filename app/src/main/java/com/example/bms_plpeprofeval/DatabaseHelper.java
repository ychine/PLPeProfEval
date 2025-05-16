package com.example.bms_plpeprofeval;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bms_plpeprofeval.models.Admin;
import com.example.bms_plpeprofeval.models.Course;
import com.example.bms_plpeprofeval.models.EssayQuestion;
import com.example.bms_plpeprofeval.models.Evaluation;
import com.example.bms_plpeprofeval.models.Professor;
import com.example.bms_plpeprofeval.models.RatingQuestion;
import com.example.bms_plpeprofeval.models.Student;
import com.example.bms_plpeprofeval.models.User;
import com.example.bms_plpeprofeval.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database helper class for the BMS Professor Evaluation application.
 * Handles all database operations including CRUD operations for users, courses,
 * questions, evaluations, and responses.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "bms_plp_eprof_eval.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_ESSAY_QUESTIONS = "essay_questions";
    private static final String TABLE_RATING_QUESTIONS = "rating_questions";
    private static final String TABLE_EVALUATIONS = "evaluations";
    private static final String TABLE_ESSAY_RESPONSES = "essay_responses";
    private static final String TABLE_RATING_RESPONSES = "rating_responses";

    // Common Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Users Table Columns
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_DEPARTMENT = "department";

    // Courses Table Columns
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_COURSE_CODE = "course_code";
    private static final String KEY_COURSE_NAME = "course_name";
    private static final String KEY_SEMESTER = "semester";
    private static final String KEY_PROFESSOR_ID = "professor_id";
    private static final String KEY_ACADEMIC_YEAR = "academic_year";

    // Questions Table Columns
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_QUESTION_TEXT = "question_text";
    private static final String KEY_IS_ACTIVE = "is_active";

    // Evaluations Table Columns
    private static final String KEY_EVALUATION_ID = "evaluation_id";
    private static final String KEY_STUDENT_ID = "student_id";
    private static final String KEY_SUBMISSION_DATE = "submission_date";
    private static final String KEY_IS_COMPLETED = "is_completed";

    // Responses Table Columns
    private static final String KEY_RESPONSE_ID = "response_id";
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_RATING = "rating";

    // Cache for frequently accessed data
    private Map<String, Professor> professorCache = new HashMap<>();
    private Map<String, Course> courseCache = new HashMap<>();

    // Create Table Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_USER_ID + " TEXT PRIMARY KEY,"
            + KEY_USER_TYPE + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_DEPARTMENT + " TEXT,"
            + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
            + KEY_COURSE_ID + " TEXT PRIMARY KEY,"
            + KEY_COURSE_CODE + " TEXT,"
            + KEY_COURSE_NAME + " TEXT,"
            + KEY_SEMESTER + " INTEGER,"
            + KEY_PROFESSOR_ID + " TEXT,"
            + KEY_ACADEMIC_YEAR + " TEXT,"
            + "FOREIGN KEY(" + KEY_PROFESSOR_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")"
            + ")";

    private static final String CREATE_TABLE_ESSAY_QUESTIONS = "CREATE TABLE " + TABLE_ESSAY_QUESTIONS + "("
            + KEY_QUESTION_ID + " TEXT PRIMARY KEY,"
            + KEY_QUESTION_TEXT + " TEXT,"
            + KEY_IS_ACTIVE + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_RATING_QUESTIONS = "CREATE TABLE " + TABLE_RATING_QUESTIONS + "("
            + KEY_QUESTION_ID + " TEXT PRIMARY KEY,"
            + KEY_QUESTION_TEXT + " TEXT,"
            + KEY_IS_ACTIVE + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_EVALUATIONS = "CREATE TABLE " + TABLE_EVALUATIONS + "("
            + KEY_EVALUATION_ID + " TEXT PRIMARY KEY,"
            + KEY_STUDENT_ID + " TEXT,"
            + KEY_PROFESSOR_ID + " TEXT,"
            + KEY_COURSE_ID + " TEXT,"
            + KEY_SUBMISSION_DATE + " DATETIME,"
            + KEY_IS_COMPLETED + " INTEGER,"
            + "FOREIGN KEY(" + KEY_STUDENT_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "),"
            + "FOREIGN KEY(" + KEY_PROFESSOR_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "),"
            + "FOREIGN KEY(" + KEY_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + KEY_COURSE_ID + ")"
            + ")";

    private static final String CREATE_TABLE_ESSAY_RESPONSES = "CREATE TABLE " + TABLE_ESSAY_RESPONSES + "("
            + KEY_RESPONSE_ID + " TEXT PRIMARY KEY,"
            + KEY_EVALUATION_ID + " TEXT,"
            + KEY_QUESTION_ID + " TEXT,"
            + KEY_RESPONSE + " TEXT,"
            + "FOREIGN KEY(" + KEY_EVALUATION_ID + ") REFERENCES " + TABLE_EVALUATIONS + "(" + KEY_EVALUATION_ID + "),"
            + "FOREIGN KEY(" + KEY_QUESTION_ID + ") REFERENCES " + TABLE_ESSAY_QUESTIONS + "(" + KEY_QUESTION_ID + ")"
            + ")";

    private static final String CREATE_TABLE_RATING_RESPONSES = "CREATE TABLE " + TABLE_RATING_RESPONSES + "("
            + KEY_RESPONSE_ID + " TEXT PRIMARY KEY,"
            + KEY_EVALUATION_ID + " TEXT,"
            + KEY_QUESTION_ID + " TEXT,"
            + KEY_RATING + " INTEGER,"
            + "FOREIGN KEY(" + KEY_EVALUATION_ID + ") REFERENCES " + TABLE_EVALUATIONS + "(" + KEY_EVALUATION_ID + "),"
            + "FOREIGN KEY(" + KEY_QUESTION_ID + ") REFERENCES " + TABLE_RATING_QUESTIONS + "(" + KEY_QUESTION_ID + ")"
            + ")";

    // Singleton instance
    private static DatabaseHelper sInstance;

    /**
     * Get singleton instance of DatabaseHelper
     *
     * @param context Application context
     * @return DatabaseHelper instance
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_ESSAY_QUESTIONS);
        db.execSQL(CREATE_TABLE_RATING_QUESTIONS);
        db.execSQL(CREATE_TABLE_EVALUATIONS);
        db.execSQL(CREATE_TABLE_ESSAY_RESPONSES);
        db.execSQL(CREATE_TABLE_RATING_RESPONSES);

        // Insert default admin
        ContentValues adminValues = new ContentValues();
        adminValues.put(KEY_USER_ID, java.util.UUID.randomUUID().toString());
        adminValues.put(KEY_USER_TYPE, Constants.USER_TYPE_ADMIN);
        adminValues.put(KEY_EMAIL, "admin@bms.edu");
        adminValues.put(KEY_PASSWORD, "admin123"); // In real app, this should be hashed
        adminValues.put(KEY_NAME, "System Administrator");
        adminValues.put(KEY_DEPARTMENT, "Information Technology");
        db.insert(TABLE_USERS, null, adminValues);

        // Insert default questions
        insertDefaultQuestions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING_RESPONSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESSAY_RESPONSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVALUATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESSAY_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create new tables
        onCreate(db);
    }

    /**
     * Helper method to insert default questions
     */
    private void insertDefaultQuestions(SQLiteDatabase db) {
        // Default rating questions
        String[] ratingQuestions = {
                "The professor is knowledgeable about the subject matter",
                "The professor explains concepts clearly",
                "The professor is well-prepared for class",
                "The professor is accessible for consultation",
                "The professor gives feedback on assessments promptly"
        };

        for (String question : ratingQuestions) {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_ID, java.util.UUID.randomUUID().toString());
            values.put(KEY_QUESTION_TEXT, question);
            values.put(KEY_IS_ACTIVE, 1);
            db.insert(TABLE_RATING_QUESTIONS, null, values);
        }

        // Default essay questions
        String[] essayQuestions = {
                "What aspects of the professor's teaching style do you find most effective?",
                "What suggestions do you have for improving the course?",
                "Additional comments about the professor's teaching"
        };

        for (String question : essayQuestions) {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_ID, java.util.UUID.randomUUID().toString());
            values.put(KEY_QUESTION_TEXT, question);
            values.put(KEY_IS_ACTIVE, 1);
            db.insert(TABLE_ESSAY_QUESTIONS, null, values);
        }
    }

    // USER METHODS

    /**
     * Add a new user to the database
     *
     * @param user User object to add
     * @return Row ID of inserted user, or -1 if error
     */
    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, user.getUserId());
            values.put(KEY_USER_TYPE, user.getUserType());
            values.put(KEY_EMAIL, user.getEmail());
            values.put(KEY_PASSWORD, user.getPassword());
            values.put(KEY_NAME, user.getFullName());
            values.put(KEY_DEPARTMENT, user.getDepartment());

            result = db.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding user: " + e.getMessage());
        }

        return result;
    }

    /**
     * Get user by email and password (for login)
     *
     * @param email    User email
     * @param password User password
     * @return User object if found, null otherwise
     */
    public User getUserByCredentials(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{email, password});
            if (cursor != null && cursor.moveToFirst()) {
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ID));
                String userType = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_TYPE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
                String department = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEPARTMENT));

                switch (userType) {
                    case Constants.USER_TYPE_STUDENT:
                        user = new Student(userId, email, password, name, department);
                        break;
                    case Constants.USER_TYPE_PROFESSOR:
                        user = new Professor(userId, email, password, name, department);
                        break;
                    case Constants.USER_TYPE_ADMIN:
                        user = new Admin(userId, email, password, name, department);
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting user by credentials: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    /**
     * Check if email exists in database
     *
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT " + KEY_EMAIL + " FROM " + TABLE_USERS +
                " WHERE " + KEY_EMAIL + " = ?";

        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.rawQuery(query, new String[]{email});
            exists = (cursor != null && cursor.getCount() > 0);
        } catch (Exception e) {
            Log.e(TAG, "Error checking if email exists: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return exists;
    }

    /**
     * Get all professors from database
     *
     * @return List of Professor objects
     */
    public List<Professor> getAllProfessors() {
        List<Professor> professors = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_TYPE + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{Constants.USER_TYPE_PROFESSOR});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String userId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ID));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
                    String department = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEPARTMENT));

                    Professor professor = new Professor(userId, email, password, name, department);
                    professors.add(professor);

                    // Cache the professor
                    professorCache.put(userId, professor);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all professors: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return professors;
    }

    /**
     * Get professor by ID
     *
     * @param professorId Professor ID
     * @return Professor object if found, null otherwise
     */
    public Professor getProfessorById(String professorId) {
        if (professorId == null) {
            return null;
        }

        // Check cache first
        if (professorCache.containsKey(professorId)) {
            return professorCache.get(professorId);
        }

        SQLiteDatabase db = getReadableDatabase();
        Professor professor = null;

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_ID + " = ? AND " + KEY_USER_TYPE + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{professorId, Constants.USER_TYPE_PROFESSOR});

            if (cursor != null && cursor.moveToFirst()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
                String department = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEPARTMENT));

                professor = new Professor(professorId, email, password, name, department);

                // Cache for future use
                professorCache.put(professorId, professor);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting professor by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return professor;
    }

    /**
     * Get all students from database
     *
     * @return List of Student objects
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_TYPE + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{Constants.USER_TYPE_STUDENT});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String userId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ID));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
                    String department = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEPARTMENT));

                    Student student = new Student(userId, email, password, name, department);
                    students.add(student);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all students: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return students;
    }

    // COURSE METHODS

    /**
     * Add a new course to the database
     *
     * @param course Course object to add
     * @return Row ID of inserted course, or -1 if error
     */
    public long addCourse(Course course) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COURSE_ID, course.getCourseId());
            values.put(KEY_COURSE_CODE, course.getCourseCode());
            values.put(KEY_COURSE_NAME, course.getCourseName());

            // Handle semester (taking first semester from list)
            if (course.getSemesterOffered() != null && !course.getSemesterOffered().isEmpty()) {
                try {
                    int semesterInt = Integer.parseInt(course.getSemesterOffered().get(0));
                    values.put(KEY_SEMESTER, semesterInt);
                } catch (NumberFormatException e) {
                    values.putNull(KEY_SEMESTER);
                }
            } else {
                values.putNull(KEY_SEMESTER);
            }

            // Handle professor (taking first professor from list)
            if (course.getProfessors() != null && !course.getProfessors().isEmpty()) {
                values.put(KEY_PROFESSOR_ID, course.getProfessors().get(0).getUserId());
            } else {
                values.putNull(KEY_PROFESSOR_ID);
            }

            // Academic Year
            values.put(KEY_ACADEMIC_YEAR, course.getAcademicYear());

            result = db.insert(TABLE_COURSES, null, values);

            // Add to cache if successful
            if (result != -1) {
                courseCache.put(course.getCourseId(), course);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding course: " + e.getMessage());
        }

        return result;
    }

    /**
     * Get all courses from database
     *
     * @return List of Course objects
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_COURSES;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String courseId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_CODE));
                    String courseName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_NAME));
                    int semester = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEMESTER));
                    String professorId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFESSOR_ID));
                    String academicYear = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACADEMIC_YEAR));

                    Course course = new Course(courseId, courseCode, courseName, semester, professorId, academicYear);
                    courses.add(course);

                    // Cache the course
                    courseCache.put(courseId, course);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all courses: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return courses;
    }

    /**
     * Get courses by professor ID
     *
     * @param professorId Professor ID
     * @return List of Course objects assigned to professor
     */
    public List<Course> getCoursesByProfessor(String professorId) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_COURSES +
                " WHERE " + KEY_PROFESSOR_ID + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{professorId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String courseId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_CODE));
                    String courseName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_NAME));
                    int semester = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEMESTER));
                    String academicYear = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACADEMIC_YEAR));

                    Course course = new Course(courseId, courseCode, courseName, semester, professorId, academicYear);
                    courses.add(course);

                    // Cache the course
                    courseCache.put(courseId, course);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting courses by professor: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return courses;
    }

    /**
     * Get course by ID
     *
     * @param courseId Course ID
     * @return Course object if found, null otherwise
     */
    public Course getCourseById(String courseId) {
        if (courseId == null) {
            return null;
        }

        // Check cache first
        if (courseCache.containsKey(courseId)) {
            return courseCache.get(courseId);
        }

        SQLiteDatabase db = getReadableDatabase();
        Course course = null;

        String query = "SELECT * FROM " + TABLE_COURSES +
                " WHERE " + KEY_COURSE_ID + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{courseId});

            if (cursor != null && cursor.moveToFirst()) {
                String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_CODE));
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_NAME));
                int semester = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SEMESTER));
                String professorId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFESSOR_ID));
                String academicYear = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACADEMIC_YEAR));

                course = new Course(courseId, courseCode, courseName, semester, professorId, academicYear);

                // Cache for future use
                courseCache.put(courseId, course);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting course by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return course;
    }

    // QUESTION METHODS

    /**
     * Get all active essay questions
     *
     * @return List of EssayQuestion objects
     */
    public List<EssayQuestion> getAllActiveEssayQuestions() {
        List<EssayQuestion> questions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ESSAY_QUESTIONS +
                " WHERE " + KEY_IS_ACTIVE + " = 1";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String questionId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUESTION_ID));
                    String questionText = cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUESTION_TEXT));
                    boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ACTIVE)) == 1;

                    EssayQuestion question = new EssayQuestion(questionId, questionText, isActive ? 1 : 0);
                    questions.add(question);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting active essay questions: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return questions;
    }

    /**
     * Get all active rating questions
     *
     * @return List of RatingQuestion objects
     */
    public List<RatingQuestion> getAllActiveRatingQuestions() {
        List<RatingQuestion> questions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_RATING_QUESTIONS +
                " WHERE " + KEY_IS_ACTIVE + " = 1";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String questionId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUESTION_ID));
                    String questionText = cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUESTION_TEXT));
                    boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ACTIVE)) == 1;

                    RatingQuestion question = new RatingQuestion(questionId, questionText, isActive ? 1 : 0);
                    questions.add(question);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting active rating questions: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return questions;
    }

    /**
     * Add a new rating question
     *
     * @param question RatingQuestion object to add
     * @return Row ID of inserted question, or -1 if error
     */
    public long addRatingQuestion(RatingQuestion question) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_ID, question.getQuestionId());
            values.put(KEY_QUESTION_TEXT, question.getQuestionText());
            values.put(KEY_IS_ACTIVE, question.isActive() ? 1 : 0);

            result = db.insert(TABLE_RATING_QUESTIONS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding rating question: " + e.getMessage());
        } finally {
            db.close();
        }
        return result;
    }
    public long addEvaluation(Evaluation evaluation) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EVALUATION_ID, java.util.UUID.randomUUID().toString());
            values.put(KEY_STUDENT_ID, evaluation.getStudentId());
            values.put(KEY_PROFESSOR_ID, evaluation.getProfessorId());
            values.put(KEY_COURSE_ID, evaluation.getCourseId());
            values.put(KEY_SUBMISSION_DATE, evaluation.getEvaluationDate());
            values.put(KEY_IS_COMPLETED, 1);

            // Insert basic evaluation data
            result = db.insert(TABLE_EVALUATIONS, null, values);

            if (result != -1) {
                String evaluationId = values.getAsString(KEY_EVALUATION_ID);

                // Save the rating responses to the database
                // We need to get the active rating questions first
                List<RatingQuestion> ratingQuestions = getAllActiveRatingQuestions();
                if (ratingQuestions.size() >= 4) {  // We have 4 rating categories in the UI
                    // Map UI ratings to questions (assuming a specific order)
                    // Teaching rating -> Question 1
                    ContentValues ratingValues = new ContentValues();
                    ratingValues.put(KEY_RESPONSE_ID, java.util.UUID.randomUUID().toString());
                    ratingValues.put(KEY_EVALUATION_ID, evaluationId);
                    ratingValues.put(KEY_QUESTION_ID, ratingQuestions.get(0).getQuestionId());
                    ratingValues.put(KEY_RATING, Math.round(evaluation.getTeachingRating()));
                    db.insert(TABLE_RATING_RESPONSES, null, ratingValues);

                    // Communication rating -> Question 2
                    ratingValues = new ContentValues();
                    ratingValues.put(KEY_RESPONSE_ID, java.util.UUID.randomUUID().toString());
                    ratingValues.put(KEY_EVALUATION_ID, evaluationId);
                    ratingValues.put(KEY_QUESTION_ID, ratingQuestions.get(1).getQuestionId());
                    ratingValues.put(KEY_RATING, Math.round(evaluation.getCommunicationRating()));
                    db.insert(TABLE_RATING_RESPONSES, null, ratingValues);

                    // Preparation rating -> Question 3
                    ratingValues = new ContentValues();
                    ratingValues.put(KEY_RESPONSE_ID, java.util.UUID.randomUUID().toString());
                    ratingValues.put(KEY_EVALUATION_ID, evaluationId);
                    ratingValues.put(KEY_QUESTION_ID, ratingQuestions.get(2).getQuestionId());
                    ratingValues.put(KEY_RATING, Math.round(evaluation.getPreparationRating()));
                    db.insert(TABLE_RATING_RESPONSES, null, ratingValues);

                    // Knowledge rating -> Question 4
                    ratingValues = new ContentValues();
                    ratingValues.put(KEY_RESPONSE_ID, java.util.UUID.randomUUID().toString());
                    ratingValues.put(KEY_EVALUATION_ID, evaluationId);
                    ratingValues.put(KEY_QUESTION_ID, ratingQuestions.get(3).getQuestionId());
                    ratingValues.put(KEY_RATING, Math.round(evaluation.getKnowledgeRating()));
                    db.insert(TABLE_RATING_RESPONSES, null, ratingValues);
                }

                // Save the comment as an essay response if provided
                if (evaluation.getComments() != null && !evaluation.getComments().isEmpty()) {
                    List<EssayQuestion> essayQuestions = getAllActiveEssayQuestions();
                    if (!essayQuestions.isEmpty()) {
                        // Use the first essay question for comments
                        ContentValues commentValues = new ContentValues();
                        commentValues.put(KEY_RESPONSE_ID, java.util.UUID.randomUUID().toString());
                        commentValues.put(KEY_EVALUATION_ID, evaluationId);
                        commentValues.put(KEY_QUESTION_ID, essayQuestions.get(0).getQuestionId());
                        commentValues.put(KEY_RESPONSE, evaluation.getComments());
                        db.insert(TABLE_ESSAY_RESPONSES, null, commentValues);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding evaluation: " + e.getMessage());
        }

        return result;
    }
}