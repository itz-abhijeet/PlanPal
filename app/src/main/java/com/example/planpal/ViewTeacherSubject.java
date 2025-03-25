package com.example.planpal;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ViewTeacherSubject extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher_subject);
        textView = findViewById(R.id.textViewAssignments);

        dbHelper = new DatabaseHelper(this);
        displayTeacherAssignments();
    }

    private void displayTeacherAssignments() {
        String[] classNames = {"Class 1", "Class 2", "Class 3"};
        StringBuilder displayText = new StringBuilder();

        for (String className : classNames) {
            List<String[]> assignments = dbHelper.viewTeacherAssignments(className);
            displayText.append(className).append(":\n\n");

            if (assignments.isEmpty()) {
                displayText.append("No assignments found\n\n");
            } else {
                for (String[] entry : assignments) {
                    displayText.append(entry[0]).append(" - ").append(entry[1]).append("\n");
                }
                displayText.append("\n\n");
            }
        }

        textView.setText(displayText.toString());
    }
}
