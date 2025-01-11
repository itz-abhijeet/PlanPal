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

    LinearLayout home_generateTimetable, home_addTeacher, home_addClassroom, home_dashboard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Initialize views
        home_generateTimetable = findViewById(R.id.Home_generateTimetable);
        home_addTeacher = findViewById(R.id.Home_addTeacher);
        home_addClassroom = findViewById(R.id.Home_addClassroom);
        home_dashboard = findViewById(R.id.Home_dashboard);

        home_generateTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_generateTimetable = new Intent(HomeActivity.this, GenerateTimetable.class);
                startActivity(intent_generateTimetable);
            }
        });

        home_addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addTeacher = new Intent(HomeActivity.this, AddTeacher.class);
                startActivity(intent_addTeacher);
            }
        });

        home_addClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addClassroom = new Intent(HomeActivity.this, AddClassroom.class);
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
    }
}
