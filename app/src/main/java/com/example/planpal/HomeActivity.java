package com.example.planpal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    LinearLayout home_generateTimetable, home_Teacher, home_Classroom, home_subject, home_teacherSubject, home_subjectClassroom, home_support, home_dashboard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Initialize views

        home_Classroom = findViewById(R.id.Home_Classroom);
        home_Teacher = findViewById(R.id.Home_Teacher);
        home_generateTimetable = findViewById(R.id.Home_generateTimetable);
        home_dashboard = findViewById(R.id.Home_dashboard);
        home_support = findViewById(R.id.Home_support);

        home_generateTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_generateTimetable = new Intent(HomeActivity.this, GenerateTimetable.class);
                startActivity(intent_generateTimetable);
            }
        });

        home_Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addTeacher = new Intent(HomeActivity.this, Teacher.class);
                startActivity(intent_addTeacher);
            }
        });

        home_Classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addClassroom = new Intent(HomeActivity.this, Classroom.class);
                startActivity(intent_addClassroom);
            }
        });

        home_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_dashboard = new Intent(HomeActivity.this, Dashboard.class);
                startActivity(intent_dashboard);
            }
        });

        home_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_support = new Intent(HomeActivity.this, ContactUs.class);
                startActivity(intent_support);
            }
        });


    }
}
