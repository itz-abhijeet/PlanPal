package com.example.planpal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTeacherSubject extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText etTeacherEmail, etSubjectCode;
    private Button btnAddTeacherSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher_subject);

        databaseHelper = new DatabaseHelper(this);

        // Reference XML views
        etTeacherEmail = findViewById(R.id.addTeacherSubject_email);
        etSubjectCode = findViewById(R.id.addTeacherSubject_subjectCode);
        btnAddTeacherSubject = findViewById(R.id.addTeacherSubject_Add);

        // Add click listener to the "Add Teacher - Subject" button
        btnAddTeacherSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeacherSubjectToDatabase();
            }
        });
    }

    private void addTeacherSubjectToDatabase() {
        // Retrieve input values
        String teacherEmail = etTeacherEmail.getText().toString().trim();
        String subjectCode = etSubjectCode.getText().toString().trim();

        // Validate inputs
        if (teacherEmail.isEmpty() || subjectCode.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(teacherEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert into the database
        boolean isInserted = databaseHelper.addTeacherSubject(teacherEmail, subjectCode);

        if (isInserted) {
            Toast.makeText(this, "Teacher-Subject mapping added successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "Failed to add mapping.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        etTeacherEmail.setText("");
        etSubjectCode.setText("");
    }
}