package com.example.planpal;

import com.example.planpal.models.Teacher;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RemoveTeacher extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeacherAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Teacher> teacherList;
    private AlertDialog confirmationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_teacher);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTeachers();

        adapter = new TeacherAdapter(this, teacherList, v -> showConfirmationDialog((Button) v));
        recyclerView.setAdapter(adapter);
    }

    private void loadTeachers() {
        Cursor cursor = dbHelper.viewTable("TeacherDB");
        teacherList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                teacherList.add(new Teacher(email, name));
            } while (cursor.moveToNext());
        }
    }

    private void showConfirmationDialog(Button button) {
        int position = recyclerView.getChildAdapterPosition((View) button.getParent());
        Teacher teacher = teacherList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Teacher")
                .setMessage("Are you sure you want to remove " + teacher.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeTeacher(teacher);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing if "No"
                })
                .show();
    }

    private void removeTeacher(Teacher teacher) {
        boolean removed = dbHelper.removeTeacher(teacher.getEmail());
        if (removed) {
            Toast.makeText(this, "Teacher removed successfully", Toast.LENGTH_SHORT).show();
            loadTeachers();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to remove teacher", Toast.LENGTH_SHORT).show();
        }
    }
}
