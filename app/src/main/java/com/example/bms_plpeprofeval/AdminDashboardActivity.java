package com.example.bms_plpeprofeval;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    EditText inputProfName, inputEssayQ, inputRatingQ;
    Button btnAddProf, btnAddEssayQ, btnAddRatingQ;
    ListView listProfs, listEssayQ, listRatingQ;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> profAdapter, essayAdapter, ratingAdapter;
    int[] profIds, essayIds, ratingIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DatabaseHelper(this);

        inputProfName = findViewById(R.id.inputProfName);
        inputEssayQ = findViewById(R.id.inputEssayQ);
        inputRatingQ = findViewById(R.id.inputRatingQ);

        btnAddProf = findViewById(R.id.btnAddProf);
        btnAddEssayQ = findViewById(R.id.btnAddEssayQ);
        btnAddRatingQ = findViewById(R.id.btnAddRatingQ);

        listProfs = findViewById(R.id.listProfs);
        listEssayQ = findViewById(R.id.listEssayQ);
        listRatingQ = findViewById(R.id.listRatingQ);

        loadAll();

        btnAddProf.setOnClickListener(v -> {
            String name = inputProfName.getText().toString().trim();
            if (!name.isEmpty()) {
                ContentValues cv = new ContentValues();
                cv.put("name", name);
                dbHelper.getWritableDatabase().insert("professors", null, cv);
                inputProfName.setText("");
                loadAll();
            }
        });

        btnAddEssayQ.setOnClickListener(v -> {
            String question = inputEssayQ.getText().toString().trim();
            if (!question.isEmpty()) {
                ContentValues cv = new ContentValues();
                cv.put("question", question);
                dbHelper.getWritableDatabase().insert("essay_questions", null, cv);
                inputEssayQ.setText("");
                loadAll();
            }
        });

        btnAddRatingQ.setOnClickListener(v -> {
            String question = inputRatingQ.getText().toString().trim();
            if (!question.isEmpty()) {
                ContentValues cv = new ContentValues();
                cv.put("question", question);
                dbHelper.getWritableDatabase().insert("rating_questions", null, cv);
                inputRatingQ.setText("");
                loadAll();
            }
        });

        listProfs.setOnItemClickListener((a, b, i, l) -> confirmDelete("professors", "name", profIds[i]));
        listEssayQ.setOnItemClickListener((a, b, i, l) -> confirmDelete("essay_questions", "question", essayIds[i]));
        listRatingQ.setOnItemClickListener((a, b, i, l) -> confirmDelete("rating_questions", "question", ratingIds[i]));
    }

    void loadAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Professors
        Cursor c1 = db.rawQuery("SELECT id, name FROM professors", null);
        String[] profNames = new String[c1.getCount()];
        profIds = new int[c1.getCount()];
        int x = 0;
        while (c1.moveToNext()) {
            profNames[x] = c1.getString(1);
            profIds[x++] = c1.getInt(0);
        }
        c1.close();
        profAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profNames);
        listProfs.setAdapter(profAdapter);

        // Essay Questions
        Cursor c2 = db.rawQuery("SELECT id, question FROM essay_questions", null);
        String[] essayQs = new String[c2.getCount()];
        essayIds = new int[c2.getCount()];
        x = 0;
        while (c2.moveToNext()) {
            essayQs[x] = c2.getString(1);
            essayIds[x++] = c2.getInt(0);
        }
        c2.close();
        essayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, essayQs);
        listEssayQ.setAdapter(essayAdapter);

        // Rating Questions
        Cursor c3 = db.rawQuery("SELECT id, question FROM rating_questions", null);
        String[] ratingQs = new String[c3.getCount()];
        ratingIds = new int[c3.getCount()];
        x = 0;
        while (c3.moveToNext()) {
            ratingQs[x] = c3.getString(1);
            ratingIds[x++] = c3.getInt(0);
        }
        c3.close();
        ratingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ratingQs);
        listRatingQ.setAdapter(ratingAdapter);
    }

    void confirmDelete(String table, String label, int id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Delete this " + label + "?")
                .setPositiveButton("Yes", (d, w) -> {
                    dbHelper.getWritableDatabase().delete(table, "id=?", new String[]{String.valueOf(id)});
                    loadAll();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
