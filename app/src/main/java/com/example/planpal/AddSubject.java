package com.example.planpal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddSubject extends AppCompatActivity {
    private EditText subjectCode, subjectName, subjectCredits;
    private CheckBox checkboxLab;
    private Button addSubjectButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        subjectCode = findViewById(R.id.addSubject_subjectCode);
        subjectName = findViewById(R.id.addSubject_subjectName);
        subjectCredits = findViewById(R.id.addSubject_subjectCredits);
        checkboxLab = findViewById(R.id.addSubject_checkboxLab);
        addSubjectButton = findViewById(R.id.addSubject_Add);

        // Set click listener for the button
        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = subjectCode.getText().toString().trim();
                String name = subjectName.getText().toString().trim();
                String creditsStr = subjectCredits.getText().toString().trim();
                boolean isLab = checkboxLab.isChecked();

                if (code.isEmpty() || name.isEmpty() || creditsStr.isEmpty()) {
                    Toast.makeText(AddSubject.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    int credits = Integer.parseInt(creditsStr);
                    boolean isAdded = databaseHelper.addSubject(code, name, credits, isLab ? "YES" : "NO");


                    if (isAdded) {
                        Toast.makeText(AddSubject.this, "Subject added successfully", Toast.LENGTH_SHORT).show();
                        subjectCode.setText("");
                        subjectName.setText("");
                        subjectCredits.setText("");
                        checkboxLab.setChecked(false);
                    } else {
                        Toast.makeText(AddSubject.this, "Error adding subject", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

