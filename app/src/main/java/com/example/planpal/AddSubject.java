package com.example.planpal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddSubject extends Activity {

    private DatabaseHelper databaseHelper;
    private EditText etSubjectCode, etSubjectName, etSubjectCredits;
    public Button btnAddSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Reference XML views
        etSubjectCode = findViewById(R.id.addSubject_subjectCode);
        etSubjectName = findViewById(R.id.addSubject_subjectName);
        etSubjectCredits = findViewById(R.id.addSubject_subjectCredits);
        btnAddSubject = findViewById(R.id.addSubject_Add);

        // Add click listener to the "Add Subject" button
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectToDatabase();
            }
        });
    }

    private void addSubjectToDatabase() {
        // Retrieve input values
        String subjectCode = etSubjectCode.getText().toString().trim();
        String subjectName = etSubjectName.getText().toString().trim();
        String creditsStr = etSubjectCredits.getText().toString().trim();

        // Validate inputs
        if (subjectCode.isEmpty() || subjectName.isEmpty() || creditsStr.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Credits must be a number!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert into the database
        boolean isInserted = databaseHelper.addSubject(subjectCode, subjectName, credits);

        if (isInserted) {
            Toast.makeText(this, "Subject added successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(this, "Failed to add subject.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        etSubjectCode.setText("");
        etSubjectName.setText("");
        etSubjectCredits.setText("");
    }
}
