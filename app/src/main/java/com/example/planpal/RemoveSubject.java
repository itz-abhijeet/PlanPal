package com.example.planpal;

import com.example.planpal.models.Subject;

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

public class RemoveSubject extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Subject> subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_subject);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSubjects();

        adapter = new SubjectAdapter(this, subjectList, v -> showConfirmationDialog((Button) v));
        recyclerView.setAdapter(adapter);
    }

    private void loadSubjects() {
        Cursor cursor = dbHelper.viewTable("SubjectDB");
        subjectList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String code = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                subjectList.add(new Subject(code, name));
            } while (cursor.moveToNext());
        }
    }

    private void showConfirmationDialog(Button button) {
        int position = recyclerView.getChildAdapterPosition((View) button.getParent());
        Subject subject = subjectList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Subject")
                .setMessage("Are you sure you want to remove " + subject.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    removeSubject(subject);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Do nothing if "No"
                })
                .show();
    }

    private void removeSubject(Subject subject) {
        boolean removed = dbHelper.removeSubject(subject.getCode());
        if (removed) {
            Toast.makeText(this, "Subject removed successfully", Toast.LENGTH_SHORT).show();
            loadSubjects();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to remove subject", Toast.LENGTH_SHORT).show();
        }
    }
}
