package com.example.planpal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeacherSubject extends AppCompatActivity {

    private Spinner spinnerClass;
    private LinearLayout subjectContainer;
    private Button btnSave;

    private DatabaseHelper dbHelper;
    private List<String> subjects;
    private List<String> teachers;
    private Map<String, String> selectedTeachers; // Mapping of subject -> selected teacher

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher_subject);

        // Initialize Views
        spinnerClass = findViewById(R.id.spinnerClass);
        subjectContainer = findViewById(R.id.subjectContainer);
        btnSave = findViewById(R.id.btnSave);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Load Classes into Spinner
        loadClasses();

        // Handle Class Selection
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadSubjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Save Button Click Listener
        btnSave.setOnClickListener(v -> saveTeacherSelections());
    }

    // Load classes into spinner
    private void loadClasses() {
        List<String> classes = new ArrayList<>();
        classes.add("Class 1");
        classes.add("Class 2");
        classes.add("Class 3");

//        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classes) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                CheckedTextView textView = (CheckedTextView) view;
                textView.setTextColor(getResources().getColor(R.color.black));

                //holder.txtSubject.setTextColor(ContextCompat.getColor(context, R.color.black));
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckedTextView textView = (CheckedTextView) view;
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        spinnerClass.setAdapter(classAdapter);
    }

    // Load subjects dynamically based on selected class
    private void loadSubjects() {
        subjectContainer.removeAllViews(); // Clear previous entries

        subjects = dbHelper.getAllSubjects();
        teachers = dbHelper.getAllTeachers();
        selectedTeachers = new HashMap<>();

        for (String subject : subjects) {
            // Create Layout for Each Subject
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            // Subject Name
            TextView subjectText = new TextView(this);
            subjectText.setText(subject);
            subjectText.setPadding(16, 16, 16, 16);
            subjectText.setTextColor(getResources().getColor(R.color.black));
            layout.addView(subjectText);

            // Teacher Selection Spinner
            Spinner teacherSpinner = new Spinner(this);
//            ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teachers);
//            teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            teacherSpinner.setAdapter(teacherAdapter);

            ArrayAdapter<String> teacherAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, teachers) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    CheckedTextView textView = (CheckedTextView) view;
                    textView.setTextColor(getResources().getColor(R.color.black));
//                    textView.setBackground(new ColorDrawable(ContextCompat.getColor(context, R.color.black)));
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                    //holder.txtSubject.setTextColor(ContextCompat.getColor(context, R.color.black));
                    return view;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    CheckedTextView textView = (CheckedTextView) view;
                    textView.setTextColor(getResources().getColor(R.color.black));
                    return view;
                }
            };

            teacherSpinner.setAdapter(teacherAdapter);

            layout.addView(teacherSpinner);

            // Handle Teacher Selection
            teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    selectedTeachers.put(subject, teachers.get(pos)); // Store teacher selection for subject
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            // Add Layout to Container
            subjectContainer.addView(layout);
        }
    }

    // Save Teacher Selections to Database (Modify This as Needed)
    private void saveTeacherSelections() {
        String selectedClass = spinnerClass.getSelectedItem().toString(); // Get selected class

        // Step 1: Delete old data
        dbHelper.deleteTeacherAssignmentsForClass(selectedClass);

        // Step 2: Insert new subject-teacher mapping
        for (Map.Entry<String, String> entry : selectedTeachers.entrySet()) {
            String subject = entry.getKey();
            String teacher = entry.getValue();

            boolean success = dbHelper.assignTeacherToSubject(selectedClass, subject, teacher);
            if (success) {
                System.out.println("Saved: " + selectedClass + " -> " + subject + " -> " + teacher);
            } else {
                System.out.println("Failed to save: " + selectedClass + " -> " + subject + " -> " + teacher);
            }
        }
    }


}
