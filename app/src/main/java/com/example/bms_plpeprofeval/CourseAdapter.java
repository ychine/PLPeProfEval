package com.example.bms_plpeprofeval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bms_plpeprofeval.DatabaseHelper;
import com.example.bms_plpeprofeval.R;
import com.example.bms_plpeprofeval.models.Course;
import com.example.bms_plpeprofeval.models.Professor;
import com.example.bms_plpeprofeval.models.Student;
import com.example.bms_plpeprofeval.utils.SessionManager;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courses;
    private DatabaseHelper databaseHelper;
    private OnCourseClickListener listener;
    private String currentStudentId;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CourseAdapter(Context context, List<Course> courses, DatabaseHelper databaseHelper, OnCourseClickListener listener) {
        this.context = context;
        this.courses = courses;
        this.databaseHelper = databaseHelper;
        this.listener = listener;

        // Get current student ID
        SessionManager sessionManager = SessionManager.getInstance(context);
        if (sessionManager.getUser() instanceof Student) {
            currentStudentId = ((Student) sessionManager.getUser()).getUserId();
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);

        // Set course details
        holder.textViewCourseName.setText(course.getCourseName());
        holder.textViewCourseCode.setText(course.getCourseCode());

        // Get professor details
        Professor professor = databaseHelper.getProfessorById(course.getProfessorId());
        if (professor != null) {
            holder.textViewProfessorName.setText("Professor: " + professor.getName());

            if (professor.getDepartment() != null && !professor.getDepartment().isEmpty()) {
                holder.textViewDepartment.setText("Department: " + professor.getDepartment());
                holder.textViewDepartment.setVisibility(View.VISIBLE);
            } else {
                holder.textViewDepartment.setVisibility(View.GONE);
            }
        } else {
            holder.textViewProfessorName.setText("Professor: Not Assigned");
            holder.textViewDepartment.setVisibility(View.GONE);
        }

        // Check if already evaluated
        boolean isEvaluated = false;
        if (currentStudentId != null) {
            isEvaluated = databaseHelper.hasStudentEvaluated(
                    currentStudentId,
                    course.getProfessorId(),
                    course.getCourseId());
        }

        // Set status indicator
        if (isEvaluated) {
            holder.imageViewStatus.setImageResource(android.R.drawable.checkbox_on_background);
            holder.imageViewStatus.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.textViewStatus.setText("Evaluated");
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else {
            holder.imageViewStatus.setImageResource(android.R.drawable.checkbox_off_background);
            holder.imageViewStatus.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray));
            holder.textViewStatus.setText("Not Evaluated");
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        // Set card background based on evaluation status
        if (isEvaluated) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCourseClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void updateCourses(List<Course> newCourses) {
        this.courses = newCourses;
        notifyDataSetChanged();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textViewCourseName;
        TextView textViewCourseCode;
        TextView textViewProfessorName;
        TextView textViewDepartment;
        TextView textViewStatus;
        ImageView imageViewStatus;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
            textViewCourseCode = itemView.findViewById(R.id.textViewCourseCode);
            textViewProfessorName = itemView.findViewById(R.id.textViewProfessorName);
            textViewDepartment = itemView.findViewById(R.id.textViewDepartment);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            imageViewStatus = itemView.findViewById(R.id.imageViewStatus);
        }
    }
}