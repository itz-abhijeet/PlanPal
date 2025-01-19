package com.example.planpal;

import com.example.planpal.models.TeacherSubject;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RemoveTeacherSubject extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TeacherSubjectAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<TeacherSubject> teacherSubjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_teacher_subject);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTeacherSubjects();

        adapter = new TeacherSubjectAdapter(this, teacherSubjectList, this::showConfirmationDialog);
        recyclerView.setAdapter(adapter);
    }

    private void loadTeacherSubjects() {
        Cursor cursor = dbHelper.viewTable("TeacherSubjectDB");
        teacherSubjectList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String teacherEmail = cursor.getString(cursor.getColumnIndexOrThrow("teacher_email"));
                String subjectCode = cursor.getString(cursor.getColumnIndexOrThrow("subject_code"));
                teacherSubjectList.add(new TeacherSubject(teacherEmail, subjectCode));
            } while (cursor.moveToNext());
        }
    }

    private void showConfirmationDialog(int position) {
        TeacherSubject teacherSubject = teacherSubjectList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Assignment")
                .setMessage("Are you sure you want to remove the assignment of Subject Code: "
                        + teacherSubject.getSubjectCode()
                        + " from Teacher: " + teacherSubject.getTeacherEmail() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeTeacherSubject(teacherSubject, position);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void removeTeacherSubject(TeacherSubject teacherSubject, int position) {
        boolean removed = dbHelper.removeTeacherSubject(teacherSubject.getTeacherEmail(), teacherSubject.getSubjectCode());
        if (removed) {
            teacherSubjectList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Assignment removed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to remove assignment", Toast.LENGTH_SHORT).show();
        }
    }
}
