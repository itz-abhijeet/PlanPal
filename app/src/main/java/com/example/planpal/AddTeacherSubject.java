package com.example.planpal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTeacherSubject extends AppCompatActivity {
    private DatabaseHelper db;
    private LinearLayout layoutClass1, layoutClass2, layoutClass3;
    private List<String> subjects;
    private List<String> teachers;
    private Map<String, Spinner> selectedTeachersMap = new HashMap<>(); // Stores dropdown selections

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher_subject);

        db = new DatabaseHelper(this);
        layoutClass1 = findViewById(R.id.layoutClass1);
        layoutClass2 = findViewById(R.id.layoutClass2);
        layoutClass3 = findViewById(R.id.layoutClass3);
        Button btnSave = findViewById(R.id.btnSave);

        subjects = getSubjectsFromDB();
        teachers = getTeachersFromDB();

        createDropdowns(layoutClass1, "Class 1");
        createDropdowns(layoutClass2, "Class 2");
        createDropdowns(layoutClass3, "Class 3");

        btnSave.setOnClickListener(v -> saveAssignments());

        db.logTeacherSubjectDB();
    }

    private List<String> getSubjectsFromDB() {
        List<String> subjectList = new ArrayList<>();
        Cursor cursor = db.viewTable(DatabaseHelper.TABLE_SUBJECT);
        if (cursor.moveToFirst()) {
            do {
                subjectList.add(cursor.getString(1)); // Get subject name
            } while (cursor.moveToNext());
        }
        cursor.close();
        return subjectList;
    }

    private List<String> getTeachersFromDB() {
        return db.getAllTeachers();
    }

//    private void createDropdowns(LinearLayout layout, String className) {
//        for (String subject : subjects) {
//            TextView textView = new TextView(this);
//            textView.setText(subject);
//            textView.setTextSize(18);
//            textView.setTextColor(Color.BLACK);
//
//            Spinner spinner = new Spinner(this);
////            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teachers);
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, teachers) {
//                @Override
//                public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                    View view = super.getDropDownView(position, convertView, parent);
//                    CheckedTextView textView = (CheckedTextView) view;
//                    textView.setTextColor(getResources().getColor(R.color.black));
//                    textView.setBackgroundColor(Color.WHITE);
//                    return view;
//                }
//
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    View view = super.getView(position, convertView, parent);
//                    CheckedTextView textView = (CheckedTextView) view;
//                    textView.setTextColor(getResources().getColor(R.color.black));
//                    textView.setBackgroundColor(Color.TRANSPARENT);
//                    return view;
//                }
//            };
//            spinner.setAdapter(adapter);
//
//
//            selectedTeachersMap.put(className + "_" + subject, spinner); // Store for retrieval
//
//            layout.addView(textView);
//            layout.addView(spinner);
//        }
//    }

    private void createDropdowns(LinearLayout layout, String className) {
        for (String subject : subjects) {
            TextView textView = new TextView(this);
            textView.setText(subject);
            textView.setTextSize(18);
            textView.setTextColor(Color.BLACK);

            Spinner spinner = new Spinner(this);

            // Fetch the pre-assigned teacher for the subject and class
            String preAssignedTeacher = getAssignedTeacher(className, subject);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, teachers) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    CheckedTextView textView = (CheckedTextView) view;
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setBackgroundColor(Color.WHITE);
                    return view;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    CheckedTextView textView = (CheckedTextView) view;
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setBackgroundColor(Color.TRANSPARENT);
                    return view;
                }
            };
            spinner.setAdapter(adapter);

            // Set the pre-assigned teacher as the selected item
            if (preAssignedTeacher != null) {
                int position = teachers.indexOf(preAssignedTeacher);
                if (position >= 0) {
                    spinner.setSelection(position);
                }
            }

            selectedTeachersMap.put(className + "_" + subject, spinner); // Store for retrieval

            layout.addView(textView);
            layout.addView(spinner);
        }
    }

    private String getAssignedTeacher(String className, String subjectName) {
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.TEACHER_SUBJECT_TEACHER +
                " FROM " + DatabaseHelper.TABLE_TEACHER_SUBJECT +
                " WHERE " + DatabaseHelper.TEACHER_SUBJECT_CLASS + "=? AND " +
                DatabaseHelper.TEACHER_SUBJECT_SUBJECT + "=?", new String[]{className, subjectName});

        if (cursor.moveToFirst()) {
            String teacherName = cursor.getString(0);
            cursor.close();
            return teacherName;
        }

        cursor.close();
        return null; // No pre-assigned teacher
    }



//    private void saveAssignments() {
//        // Step 1: Delete old assignments for all 3 classes
//        db.deleteTeacherAssignmentsForClass("Class 1");
//        db.deleteTeacherAssignmentsForClass("Class 2");
//        db.deleteTeacherAssignmentsForClass("Class 3");
//
//        // Step 2: Insert new assignments
//        for (Map.Entry<String, Spinner> entry : selectedTeachersMap.entrySet()) {
//            String key = entry.getKey(); // Example: "Class 1_Subject X"
//            String[] parts = key.split("_");
//
//            if (parts.length < 2) {
//                Log.e("SAVE_ASSIGNMENTS", "Invalid key format: " + key);
//                continue;
//            }
//
//            String className = parts[0];  // "Class 1", "Class 2", etc.
//            String subject = parts[1];    // "Math", "Physics", etc.
//            String teacher = entry.getValue().getSelectedItem() != null ? entry.getValue().getSelectedItem().toString() : "";
//
//            if (teacher.isEmpty()) {
//                Log.d("SAVE_ASSIGNMENTS", "Skipping empty teacher for " + className + " - " + subject);
//                continue; // Skip if no teacher is selected
//            }
//
//            Log.d("SAVE_ASSIGNMENTS", "Assigning: Class=" + className + ", Subject=" + subject + ", Teacher=" + teacher);
//
//            db.assignTeacherToSubject(className, subject, teacher);
//        }
//
//        Toast.makeText(this, "Assignments updated!", Toast.LENGTH_SHORT).show();
//    }

    private void saveAssignments() {
        // Step 1: Delete old assignments for all 3 classes
        db.deleteTeacherAssignmentsForClass("Class 1");
        db.deleteTeacherAssignmentsForClass("Class 2");
        db.deleteTeacherAssignmentsForClass("Class 3");

        // Step 2: Insert new assignments
        for (Map.Entry<String, Spinner> entry : selectedTeachersMap.entrySet()) {
            String key = entry.getKey(); // Example: "Class 1_Subject X"
            String[] parts = key.split("_");

            if (parts.length < 2) {
                Log.e("SAVE_ASSIGNMENTS", "Invalid key format: " + key);
                continue;
            }

            String className = parts[0];  // "Class 1", "Class 2", etc.
            String subject = parts[1];    // "Math", "Physics", etc.
            String teacher = entry.getValue().getSelectedItem() != null ? entry.getValue().getSelectedItem().toString() : "";

            if (teacher.isEmpty()) {
                Log.d("SAVE_ASSIGNMENTS", "Skipping empty teacher for " + className + " - " + subject);
                continue; // Skip if no teacher is selected
            }

            // Fetch subject code from the database
            String subjectCode = getSubjectCode(subject);
            if (subjectCode == null) {
                Log.e("SAVE_ASSIGNMENTS", "No subject code found for " + subject);
                continue;
            }

            // Fetch teacher email from the database
            String teacherEmail = getTeacherEmail(teacher);
            if (teacherEmail == null) {
                Log.e("SAVE_ASSIGNMENTS", "No email found for teacher " + teacher);
                continue;
            }

            Log.d("SAVE_ASSIGNMENTS", "Assigning: Class=" + className + ", Subject=" + subject +
                    ", Subject Code=" + subjectCode + ", Teacher=" + teacher + ", Teacher Email=" + teacherEmail);

            db.assignTeacherToSubject(className, subject, teacher, subjectCode, teacherEmail);
        }

        Toast.makeText(this, "Assignments updated!", Toast.LENGTH_SHORT).show();
    }

    private String getSubjectCode(String subjectName) {
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.SUBJECT_CODE +
                " FROM " + DatabaseHelper.TABLE_SUBJECT +
                " WHERE " + DatabaseHelper.SUBJECT_NAME + "=?", new String[]{subjectName});

        if (cursor.moveToFirst()) {
            String code = cursor.getString(0);
            cursor.close();
            return code;
        }

        cursor.close();
        return null; // Not found
    }

    private String getTeacherEmail(String teacherName) {
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.TEACHER_EMAIL +
                " FROM " + DatabaseHelper.TABLE_TEACHER +
                " WHERE " + DatabaseHelper.TEACHER_NAME + "=?", new String[]{teacherName});

        if (cursor.moveToFirst()) {
            String email = cursor.getString(0);
            cursor.close();
            return email;
        }

        cursor.close();
        return null; // Not found
    }


}
