package com.example.planpal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        home_subject = findViewById(R.id.Home_subject);
        home_teacherSubject = findViewById(R.id.Home_teacherSubject);
//        home_subjectClassroom = findViewById(R.id.Home_subjectClassroom);
        home_generateTimetable = findViewById(R.id.Home_generateTimetable);
        home_dashboard = findViewById(R.id.Home_dashboard);
        home_support = findViewById(R.id.Home_support);


        home_Classroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Classroom = new Intent(HomeActivity.this, Classroom.class);
                startActivity(intent_Classroom);

//                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        home_Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Teacher = new Intent(HomeActivity.this, Teacher.class);
                startActivity(intent_Teacher);

//                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        home_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_subject = new Intent(HomeActivity.this, Subject.class);
                startActivity(intent_subject);
            }
        });

        home_teacherSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_teacherSubject = new Intent(HomeActivity.this, TeacherSubject.class);
                startActivity(intent_teacherSubject);

//                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

//        home_subjectClassroom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent_subjectClassroom = new Intent(HomeActivity.this, SubjectClassroom.class);
////                startActivity(intent_subjectClassroom);
//                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
//            }
//        });

        home_generateTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_generateTimetable = new Intent(HomeActivity.this, GenerateTimetable.class);
                startActivity(intent_generateTimetable);
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
