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
import java.util.List;

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

    // Get singleton instance
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

    // Helper method to insert default questions
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

    // Add a new user
    public long addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, user.getUserId());
            values.put(KEY_USER_TYPE, user.getUserType());
            values.put(KEY_EMAIL, user.getEmail());
            values.put(KEY_PASSWORD, user.getPassword());
            values.put(KEY_NAME, user.getName());
            values.put(KEY_DEPARTMENT, user.getDepartment());

            result = db.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding user: " + e.getMessage());
        }

        return result;
    }

    // Get user by email and password (login)
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

    // Check if email exists
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

    // Get all professors
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

    // Get all students
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

    // Add a new course
    public long addCourse(Course course) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COURSE_ID, course.getCourseId());
            values.put(KEY_COURSE_CODE, course.getCourseCode());
            values.put(KEY_COURSE_NAME, course.getCourseName());
            values.put(KEY_SEMESTER, course.getSemester());
            values.put(KEY_PROFESSOR_ID, course.getProfessorId());
            values.put(KEY_ACADEMIC_YEAR, course.getAcademicYear());

            result = db.insert(TABLE_COURSES, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding course: " + e.getMessage());
        }

        return result;
    }

    // Get all courses
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

    // Get courses by professor
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

    // QUESTION METHODS

    // Get all active essay questions
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

                    EssayQuestion question = new EssayQuestion(questionId, questionText, isActive);
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

    // Get all active rating questions
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

                    RatingQuestion question = new RatingQuestion(questionId, questionText, isActive);
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

    // Add a new essay question
    public long addEssayQuestion(EssayQuestion question) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_ID, question.getQuestionId());
            values.put(KEY_QUESTION_TEXT, question.getQuestionText());
            values.put(KEY_IS_ACTIVE, question.isActive() ? 1 : 0);

            result = db.insert(TABLE_ESSAY_QUESTIONS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error adding essay question: " + e.getMessage());
        }

        return result;
    }

    // Add a new rating question
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
        }

        return result;
    }

    // Update essay question
    public int updateEssayQuestion(EssayQuestion question) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_TEXT, question.getQuestionText());
            values.put(KEY_IS_ACTIVE, question.isActive() ? 1 : 0);

            result = db.update(TABLE_ESSAY_QUESTIONS, values,
                    KEY_QUESTION_ID + " = ?",
                    new String[]{question.getQuestionId()});
        } catch (Exception e) {
            Log.e(TAG, "Error updating essay question: " + e.getMessage());
        }

        return result;
    }

    // Update rating question
    public int updateRatingQuestion(RatingQuestion question) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_TEXT, question.getQuestionText());
            values.put(KEY_IS_ACTIVE, question.isActive() ? 1 : 0);

            result = db.update(TABLE_RATING_QUESTIONS, values,
                    KEY_QUESTION_ID + " = ?",
                    new String[]{question.getQuestionId()});
        } catch (Exception e) {
            Log.e(TAG, "Error updating rating question: " + e.getMessage());
        }

        return result;
    }

    // EVALUATION METHODS

    // Add a new evaluation
    public long createEvaluation(Evaluation evaluation) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_EVALUATION_ID, evaluation.getEvaluationId());
            values.put(KEY_STUDENT_ID, evaluation.getStudentId());
            values.put(KEY_PROFESSOR_ID, evaluation.getProfessorId());
            values.put(KEY_COURSE_ID, evaluation.getCourseId());
            values.put(KEY_SUBMISSION_DATE, evaluation.getSubmissionDate());
            values.put(KEY_IS_COMPLETED, evaluation.isCompleted() ? 1 : 0);

            result = db.insert(TABLE_EVALUATIONS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error creating evaluation: " + e.getMessage());
        }

        return result;
    }

    // Save essay response
    public long saveEssayResponse(String responseId, String evaluationId, String questionId, String response) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RESPONSE_ID, responseId);
            values.put(KEY_EVALUATION_ID, evaluationId);
            values.put(KEY_QUESTION_ID, questionId);
            values.put(KEY_RESPONSE, response);

            result = db.insert(TABLE_ESSAY_RESPONSES, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error saving essay response: " + e.getMessage());
        }

        return result;
    }

    // Save rating response
    public long saveRatingResponse(String responseId, String evaluationId, String questionId, int rating) {
        SQLiteDatabase db = getWritableDatabase();
        long result = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RESPONSE_ID, responseId);
            values.put(KEY_EVALUATION_ID, evaluationId);
            values.put(KEY_QUESTION_ID, questionId);
            values.put(KEY_RATING, rating);

            result = db.insert(TABLE_RATING_RESPONSES, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error saving rating response: " + e.getMessage());
        }

        return result;
    }

    // Complete evaluation
    public int completeEvaluation(String evaluationId, String submissionDate) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_SUBMISSION_DATE, submissionDate);
            values.put(KEY_IS_COMPLETED, 1);

            result = db.update(TABLE_EVALUATIONS, values,
                    KEY_EVALUATION_ID + " = ?",
                    new String[]{evaluationId});
        } catch (Exception e) {
            Log.e(TAG, "Error completing evaluation: " + e.getMessage());
        }

        return result;
    }

    // Get evaluations by student
    public List<Evaluation> getEvaluationsByStudent(String studentId) {
        List<Evaluation> evaluations = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EVALUATIONS +
                " WHERE " + KEY_STUDENT_ID + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{studentId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String evaluationId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVALUATION_ID));
                    String professorId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PROFESSOR_ID));
                    String courseId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String submissionDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUBMISSION_DATE));
                    boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_COMPLETED)) == 1;

                    Evaluation evaluation = new Evaluation(evaluationId, studentId, professorId, courseId, submissionDate, isCompleted);
                    evaluations.add(evaluation);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting evaluations by student: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return evaluations;
    }

    // Get evaluations by professor
    public List<Evaluation> getEvaluationsByProfessor(String professorId) {
        List<Evaluation> evaluations = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_EVALUATIONS +
                " WHERE " + KEY_PROFESSOR_ID + " = ? AND " + KEY_IS_COMPLETED + " = 1";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{professorId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String evaluationId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVALUATION_ID));
                    String studentId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_STUDENT_ID));
                    String courseId = cursor.getString(cursor.getColumnIndexOrThrow(KEY_COURSE_ID));
                    String submissionDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SUBMISSION_DATE));
                    boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_COMPLETED)) == 1;

                    Evaluation evaluation = new Evaluation(evaluationId, studentId, professorId, courseId, submissionDate, isCompleted);
                    evaluations.add(evaluation);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting evaluations by professor: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return evaluations;
    }

    // Get evaluation results (for professor view)
    public List<String> getEssayResponsesForEvaluation(String evaluationId) {
        List<String> responses = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT er." + KEY_RESPONSE + ", eq." + KEY_QUESTION_TEXT +
                " FROM " + TABLE_ESSAY_RESPONSES + " er" +
                " JOIN " + TABLE_ESSAY_QUESTIONS + " eq ON er." + KEY_QUESTION_ID + " = eq." + KEY_QUESTION_ID +
                " WHERE er." + KEY_EVALUATION_ID + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{evaluationId});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String questionText = cursor.getString(cursor.getColumnIndexOrThrow(KEY_QUESTION_TEXT));
                    String response = cursor.getString(cursor.getColumnIndexOrThrow(KEY_RESPONSE));

                    responses.add(questionText + ": " + response);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting essay responses: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return responses;
    }

    // Get rating averages for a professor
    public float getAverageRatingForProfessor(String professorId, String questionId) {
        SQLiteDatabase db = getReadableDatabase();
        float average = 0;

        String query = "SELECT AVG(rr." + KEY_RATING + ") AS average_rating" +
                " FROM " + TABLE_RATING_RESPONSES + " rr" +
                " JOIN " + TABLE_EVALUATIONS + " e ON rr." + KEY_EVALUATION_ID + " = e." + KEY_EVALUATION_ID +
                " WHERE e." + KEY_PROFESSOR_ID + " = ? AND rr." + KEY_QUESTION_ID + " = ? AND e." + KEY_IS_COMPLETED + " = 1";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{professorId, questionId});

            if (cursor != null && cursor.moveToFirst()) {
                average = cursor.getFloat(cursor.getColumnIndexOrThrow("average_rating"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting average rating: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return average;
    }

    // Check if student has already evaluated a course/professor
    public boolean hasStudentEvaluated(String studentId, String professorId, String courseId) {
        SQLiteDatabase db = getReadableDatabase();
        boolean hasEvaluated = false;

        String query = "SELECT COUNT(*) AS count FROM " + TABLE_EVALUATIONS +
                " WHERE " + KEY_STUDENT_ID + " = ? AND " +
                KEY_PROFESSOR_ID + " = ? AND " +
                KEY_COURSE_ID + " = ? AND " +
                KEY_IS_COMPLETED + " = 1";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{studentId, professorId, courseId});

            if (cursor != null && cursor.moveToFirst()) {
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
                hasEvaluated = (count > 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking student evaluation: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return hasEvaluated;
    }

    // Get user by ID
    public User getUserById(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + KEY_USER_ID + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{userId});

            if (cursor != null && cursor.moveToFirst()) {
                String userType = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_TYPE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD));
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
            Log.e(TAG, "Error getting user by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }

    // Get course by ID
    public Course getCourseById(String courseId) {
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
}