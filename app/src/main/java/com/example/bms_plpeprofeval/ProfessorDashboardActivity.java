package com.example.bms_plpeprofeval;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class ProfessorDashboardActivity extends AppCompatActivity {
    int profId;
    DatabaseHelper dbHelper;
    TableLayout ratingTable;
    LinearLayout essayContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_dashboard);

        // Get professor ID from Intent
        profId = getIntent().getIntExtra("prof_id", -1);
        dbHelper = new DatabaseHelper(this);

        ratingTable = findViewById(R.id.ratingTable);
        essayContainer = findViewById(R.id.essayContainer);

        loadRatings();
        loadEssays();
    }

    void loadRatings() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor qCursor = db.rawQuery("SELECT id, question FROM rating_questions", null);
        while (qCursor.moveToNext()) {
            int qId = qCursor.getInt(0);
            String question = qCursor.getString(1);

            Cursor avgCursor = db.rawQuery("SELECT AVG(rating) FROM ratings WHERE prof_id=? AND question_id=?", new String[]{String.valueOf(profId), String.valueOf(qId)});
            double avg = 0;
            if (avgCursor.moveToFirst()) avg = avgCursor.getDouble(0);
            avgCursor.close();

            TableRow row = new TableRow(this);
            TextView qText = new TextView(this);
            qText.setText(question);
            TextView avgText = new TextView(this);
            avgText.setText(String.format("%.2f", avg));

            row.addView(qText);
            row.addView(avgText);
            ratingTable.addView(row);
        }
        qCursor.close();
    }

    void loadEssays() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT question_id, answer FROM essays WHERE prof_id=?", new String[]{String.valueOf(profId)});

        while (cursor.moveToNext()) {
            int qId = cursor.getInt(0);
            String answer = cursor.getString(1);

            String question = "";
            Cursor q = db.rawQuery("SELECT question FROM essay_questions WHERE id=?", new String[]{String.valueOf(qId)});
            if (q.moveToFirst()) question = q.getString(0);
            q.close();

            TextView qView = new TextView(this);
            qView.setText("Q: " + question);
            TextView aView = new TextView(this);
            aView.setText("Feedback: " + answer);
            aView.setPadding(0, 0, 0, 30);

            essayContainer.addView(qView);
            essayContainer.addView(aView);
        }

        cursor.close();
    }
}
