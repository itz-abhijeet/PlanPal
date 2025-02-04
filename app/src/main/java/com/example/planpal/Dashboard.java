package com.example.planpal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    ImageButton Dashboard_back;
    Button Dashboard_ContactUs, Dashboard_PregeneratedTimetable;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        Dashboard_back = findViewById(R.id.Dashboard_back);
        Dashboard_ContactUs = findViewById(R.id.Dashboard_ContactUs);
        Dashboard_PregeneratedTimetable = findViewById(R.id.Dashboard_PregeneratedTimetable);


        Dashboard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_homepage = new Intent(Dashboard.this, HomeActivity.class);
                startActivity(intent_homepage);
            }
        });

        Dashboard_PregeneratedTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_pregenerated = new Intent(Dashboard.this, PregeneratedTimetables.class);
                startActivity(intent_pregenerated);
            }
        });

        Dashboard_ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_homepage = new Intent(Dashboard.this, ContactUs.class);
                startActivity(intent_homepage);
            }
        });


    }
}