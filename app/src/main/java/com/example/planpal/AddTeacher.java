package com.example.planpal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddTeacher extends AppCompatActivity {

    TextView teacherName, teacherEmail;
    Button addTeacher;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher);

        databaseHelper = new DatabaseHelper(this); // Initialize the database helper
        teacherName = findViewById(R.id.addTeacher_name); // Find teacher name input field
        teacherEmail = findViewById(R.id.addTeacher_email); // Find teacher email input field
        addTeacher = findViewById(R.id.addTeacher_Add); // Find add teacher button

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get input values
                String name = teacherName.getText().toString().trim();
                String email = teacherEmail.getText().toString().trim();

                // Validate input fields
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(AddTeacher.this, "Fill the fields properly", Toast.LENGTH_SHORT).show();
                } else {
                    // Attempt to add teacher to database
                    boolean isAdded = databaseHelper.addTeacher(email, name);
                    if (isAdded) {
                        Toast.makeText(AddTeacher.this, "Teacher added successfully", Toast.LENGTH_SHORT).show();
                        teacherName.setText(""); // Clear the input fields
                        teacherEmail.setText("");
                    } else {
                        Toast.makeText(AddTeacher.this, "Error adding teacher. Email might already exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
