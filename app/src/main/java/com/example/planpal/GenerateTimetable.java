package com.example.planpal;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.io.font.FontProgramFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.Manifest;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;


public class GenerateTimetable extends AppCompatActivity {

    private TableLayout timetableTable1, timetableTable2, timetableTable3;
    private Button generateTimetableButton, savePdfButton;
    private DatabaseHelper dbHelper;
    TextView nameofInstitute, c1, c2, c3;
    private File pdfFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_timetable);

        nameofInstitute = findViewById(R.id.nameofInstitute);
        timetableTable1 = findViewById(R.id.timetableTable1);
        timetableTable2 = findViewById(R.id.timetableTable2);
        timetableTable3 = findViewById(R.id.timetableTable3);
        generateTimetableButton = findViewById(R.id.generateTimetableButton);
        dbHelper = new DatabaseHelper(this);

        savePdfButton = findViewById(R.id.savePdfButton);
        savePdfButton.setOnClickListener(v -> generatePDF());


        c1 = findViewById(R.id.class1text);
        c2 = findViewById(R.id.class2text);
        c3 = findViewById(R.id.class3text);


        generateTimetableButton.setOnClickListener(v -> generateTimetables());
    }

    private void clearTimetables() {
        timetableTable1.removeAllViews();
        timetableTable2.removeAllViews();
        timetableTable3.removeAllViews();
    }


    private TableRow createHeaderRow() {
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        TextView emptyCell = new TextView(this);
        emptyCell.setLayoutParams(new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
        ));
        emptyCell.setPadding(8, 8, 8, 8);
        headerRow.addView(emptyCell); // Empty cell for alignment

        for (int hour = 1; hour <= 6; hour++) {
            TextView hourCell = new TextView(this);
            hourCell.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
            ));
            hourCell.setPadding(8, 8, 8, 8);
            hourCell.setTextSize(16);
            hourCell.setText("Lec " + hour);
            hourCell.setTextColor(Color.BLACK);
            hourCell.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(hourCell);
        }
        return headerRow;
    }

    private Map<String, String> fetchSubjectCodeToName() {
        Map<String, String> subjectMap = new HashMap<>();
        Cursor cursor = dbHelper.viewTable(DatabaseHelper.TABLE_SUBJECT);

        while (cursor.moveToNext()) {
            String subjectCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_CODE));
            String subjectName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_NAME));
            subjectMap.put(subjectName, subjectCode);
        }

        cursor.close();
        Log.d("DEBUG", "Subject Code to Name Map: " + subjectMap);
        return subjectMap;
    }

//    private void generateTimetables() {
//        clearTimetables();
//        nameofInstitute.setVisibility(View.VISIBLE);
//        c1.setVisibility(View.VISIBLE);
//        c2.setVisibility(View.VISIBLE);
//        c3.setVisibility(View.VISIBLE);
//        savePdfButton.setVisibility(View.VISIBLE);
//
//        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT"};
//        TableLayout[] timetables = {timetableTable1, timetableTable2, timetableTable3};
//
//        // Fetch subject-teacher assignments (Stores subject codes)
//        Map<String, List<String>> subjectTeachers = fetchSubjectTeachers();
//        Log.d("DEBUG", "Fetched Subject-Teacher Assignments: " + subjectTeachers);
//
//        if (subjectTeachers.isEmpty()) {
//            Toast.makeText(this, "No teacher assignments found.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Fetch subjects with credits (Stores subject names)
//        Map<String, Integer> subjectCredits = fetchSubjectsWithCredits();
//        Log.d("DEBUG", "Fetched Subject Credits: " + subjectCredits);
//
//        if (subjectCredits.isEmpty()) {
//            Toast.makeText(this, "Subjects don't exist.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Fetch subject name → subject code mapping
//        Map<String, String> subjectCodeMap = fetchSubjectCodeToName();
//        Log.d("DEBUG", "Subject Code Mapping: " + subjectCodeMap);
//
//        // Expand subject list based on credits (Convert subject names to codes)
//        List<String> expandedSubjects = new ArrayList<>();
//        for (Map.Entry<String, Integer> entry : subjectCredits.entrySet()) {
//            String subjectName = entry.getKey();
//            String subjectCode = subjectCodeMap.get(subjectName);
//
//            if (subjectCode != null && subjectTeachers.containsKey(subjectCode)) { // Check if the subject has teachers
//                for (int i = 0; i < entry.getValue(); i++) {
//                    expandedSubjects.add(subjectCode);
//                }
//            }
//        }
//
//        if (expandedSubjects.isEmpty()) {
//            Toast.makeText(this, "No valid subjects with assigned teachers.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Teacher schedule to track availability PER CLASS
//        Map<String, Map<String, Set<String>>> teacherSchedule = new HashMap<>();
//
//        // Global schedule to prevent a teacher from being assigned to multiple classes at the same time
//        Map<String, Set<String>> globalTeacherSchedule = new HashMap<>();
//
//        // Balancing data: Lecture and "No Teacher Assigned" counts per class
//        Map<String, Integer> classLectureCount = new HashMap<>();
//        Map<String, Integer> classNoTeacherCount = new HashMap<>();
//
//        for (int i = 0; i < timetables.length; i++) {
//            TableLayout table = timetables[i];
//            String className = "Class " + (i + 1);
//            table.addView(createHeaderRow());
//
//            for (String day : days) {
//                TableRow row = new TableRow(this);
//                row.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.WRAP_CONTENT
//                ));
//
//                TextView dayCell = new TextView(this);
//                dayCell.setLayoutParams(new TableRow.LayoutParams(
//                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
//                ));
//                dayCell.setPadding(8, 8, 8, 8);
//                dayCell.setTextSize(16);
//                dayCell.setText(day);
//                dayCell.setTextColor(Color.BLACK);
//                row.addView(dayCell);
//
//                List<String> availableSubjects = new ArrayList<>(expandedSubjects);
//                Collections.shuffle(availableSubjects);
//
//                for (int hour = 0; hour < 6; hour++) {
//                    TextView cell = new TextView(this);
//                    cell.setLayoutParams(new TableRow.LayoutParams(
//                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
//                    ));
//                    cell.setPadding(8, 8, 8, 8);
//                    cell.setTextSize(16);
//
//                    if (availableSubjects.isEmpty()) {
//                        availableSubjects = new ArrayList<>(expandedSubjects);
//                        Collections.shuffle(availableSubjects);
//                    }
//
//                    String subjectCode = availableSubjects.remove(0);
//                    Log.d("DEBUG", "Assigning teacher for Subject Code: " + subjectCode + " on " + day + " hour " + hour);
//
//                    // Assign teacher per class while preventing conflicts across all classes
//                    String assignedTeacher = assignTeacher(className, subjectCode, subjectTeachers,
//                            teacherSchedule, globalTeacherSchedule,
//                            classLectureCount, classNoTeacherCount,
//                            day, hour);
//
//                    if (assignedTeacher == null) {
//                        String sN = dbHelper.getSubjectNameBySubjectCode(subjectCode);
//                        cell.setText(sN + "\n" + "No Teacher Avaiable");
//                        cell.setTextColor(Color.RED);
//                    } else {
//                        String sN = dbHelper.getSubjectNameBySubjectCode(subjectCode);
//                        String tN = dbHelper.getTeacherNameByTeacherMail(assignedTeacher);
//                        cell.setText(sN + "\n" + tN);
//                        cell.setTextColor(Color.BLACK);
//                    }
//
//                    row.addView(cell);
//                }
//
//                table.addView(row);
//            }
//        }
//
//        Log.d("DEBUG", "Class Lecture Counts: " + classLectureCount);
//        Log.d("DEBUG", "Class No Teacher Counts: " + classNoTeacherCount);
//    }

    private void generateTimetables() {
        clearTimetables();
        nameofInstitute.setVisibility(View.VISIBLE);
        c1.setVisibility(View.VISIBLE);
        c2.setVisibility(View.VISIBLE);
        c3.setVisibility(View.VISIBLE);
        savePdfButton.setVisibility(View.VISIBLE);

        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT"};
        TableLayout[] timetables = {timetableTable1, timetableTable2, timetableTable3};

        // Fetch subject-teacher assignments (Stores subject codes)
        Map<String, List<String>> subjectTeachers = fetchSubjectTeachers();
        if (subjectTeachers.isEmpty()) {
            Toast.makeText(this, "No teacher assignments found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch subjects with credits (Stores subject names)
        Map<String, Integer> subjectCredits = fetchSubjectsWithCredits();
        if (subjectCredits.isEmpty()) {
            Toast.makeText(this, "Subjects don't exist.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch subject name → subject code mapping
        Map<String, String> subjectCodeMap = fetchSubjectCodeToName();

        // Expand subject list based on credits (Convert subject names to codes)
        List<String> expandedSubjects = new ArrayList<>();
        Map<String, Map<String, Integer>> classSubjectLectureCount = new HashMap<>();

        for (Map.Entry<String, Integer> entry : subjectCredits.entrySet()) {
            String subjectName = entry.getKey();
            String subjectCode = subjectCodeMap.get(subjectName);

            if (subjectCode != null && subjectTeachers.containsKey(subjectCode)) {
                classSubjectLectureCount.putIfAbsent(subjectCode, new HashMap<>());

                for (int i = 0; i < entry.getValue(); i++) {
                    if (entry.getValue() == 1) {
                        boolean canAdd = true;
                        for (String className : new String[]{"Class 1", "Class 2", "Class 3"}) {
                            int lectureCount = classSubjectLectureCount.get(subjectCode).getOrDefault(className, 0);
                            if (lectureCount >= 1) {
                                canAdd = false;
                                break;
                            }
                        }
                        if (!canAdd) break;
                    }
                    expandedSubjects.add(subjectCode);
                }
            }
        }

        if (expandedSubjects.isEmpty()) {
            Toast.makeText(this, "No valid subjects with assigned teachers.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Teacher schedule to track availability PER CLASS
        Map<String, Map<String, Set<String>>> teacherSchedule = new HashMap<>();

        // Global schedule to prevent a teacher from being assigned to multiple classes at the same time
        Map<String, Set<String>> globalTeacherSchedule = new HashMap<>();

        // Balancing data: Lecture and "No Teacher Assigned" counts per class
        Map<String, Integer> classLectureCount = new HashMap<>();
        Map<String, Integer> classNoTeacherCount = new HashMap<>();
        // Track how many times a subject has been assigned to each class
        Map<String, Integer> subjectClassCount = new HashMap<>();


        for (int i = 0; i < timetables.length; i++) {
            TableLayout table = timetables[i];
            String className = "Class " + (i + 1);
            table.addView(createHeaderRow());

            for (String day : days) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                TextView dayCell = new TextView(this);
                dayCell.setLayoutParams(new TableRow.LayoutParams(
                        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
                ));

                dayCell.setPadding(8, 8, 8, 8);
                dayCell.setTextSize(16);
                dayCell.setText(day);
                dayCell.setTextColor(Color.BLACK);
                row.addView(dayCell);

                List<String> availableSubjects = new ArrayList<>(expandedSubjects);
                Collections.shuffle(availableSubjects);

                for (int hour = 0; hour < 6; hour++) {
                    TextView cell = new TextView(this);
                    cell.setLayoutParams(new TableRow.LayoutParams(
                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
                    ));
                    cell.setPadding(8, 8, 8, 8);
                    cell.setTextSize(16);

                    if (availableSubjects.isEmpty()) {
                        availableSubjects = new ArrayList<>(expandedSubjects);
                        Collections.shuffle(availableSubjects);
                    }

                    String subjectCode = availableSubjects.remove(0);

                    String assignedTeacher = assignTeacher(className, subjectCode, subjectTeachers,
                            teacherSchedule, globalTeacherSchedule,
                            classLectureCount, classNoTeacherCount,
                            subjectCredits, subjectClassCount,
                            day, hour);


                    if (assignedTeacher == null) {
                        String sN = dbHelper.getSubjectNameBySubjectCode(subjectCode);
                        cell.setText(sN + "\n" + "No Teacher Available");
                        cell.setTextColor(Color.RED);
                    } else {
                        String sN = dbHelper.getSubjectNameBySubjectCode(subjectCode);
                        String tN = dbHelper.getTeacherNameByTeacherMail(assignedTeacher);
                        cell.setText(sN + "\n" + tN);
                        cell.setTextColor(Color.BLACK);

                        // Increment the lecture count for this subject in this class
                        classSubjectLectureCount.putIfAbsent(subjectCode, new HashMap<>());
                        classSubjectLectureCount.get(subjectCode).put(className,
                                classSubjectLectureCount.get(subjectCode).getOrDefault(className, 0) + 1);
                    }

                    row.addView(cell);
                }

                table.addView(row);
            }
        }
    }

    private String assignTeacher(String className, String subjectCode,
                                 Map<String, List<String>> subjectTeachers,
                                 Map<String, Map<String, Set<String>>> teacherSchedule,
                                 Map<String, Set<String>> globalTeacherSchedule,
                                 Map<String, Integer> classLectureCount,
                                 Map<String, Integer> classNoTeacherCount,
                                 Map<String, Integer> subjectCredits,
                                 Map<String, Integer> subjectClassCount,
                                 String day, int hour) {

        List<String> teachers = subjectTeachers.get(subjectCode);
        if (teachers == null || teachers.isEmpty()) {
            Log.d("DEBUG", "No teachers available for Subject: " + subjectCode);
            classNoTeacherCount.put(className, classNoTeacherCount.getOrDefault(className, 0) + 1);
            return null;
        }

        String timeSlot = day + "-" + hour;

        teacherSchedule.putIfAbsent(className, new HashMap<>());
        teacherSchedule.get(className).putIfAbsent(timeSlot, new HashSet<>());
        globalTeacherSchedule.putIfAbsent(timeSlot, new HashSet<>());

        // Ensure 1-credit subjects get exactly 1 lecture per class per week
        int maxLectures = subjectCredits.getOrDefault(subjectCode, 3); // Default to 3 if not found
        if (maxLectures == 1) {
            int count = subjectClassCount.getOrDefault(className + "-" + subjectCode, 0);
            if (count >= 1) {
                Log.d("DEBUG", "Skipping Subject: " + subjectCode + " in " + className + " as it already has 1 lecture.");
                return null; // Don't assign more than 1 lecture for 1-credit subjects per class
            }
        }

        // Strictly assign teacher first
        for (String teacher : teachers) {
            String assignedClass = dbHelper.getAssignedClassForTeacherAndSubject(teacher, subjectCode);
            boolean isExplicitlyAssigned = className.equals(assignedClass);

            Set<String> assignedTeachersClass = teacherSchedule.get(className).get(timeSlot);
            Set<String> assignedTeachersGlobal = globalTeacherSchedule.get(timeSlot);

            if (!assignedTeachersClass.contains(teacher) && !assignedTeachersGlobal.contains(teacher)) {
                if (isExplicitlyAssigned) {
                    assignedTeachersClass.add(teacher);
                    assignedTeachersGlobal.add(teacher);
                    classLectureCount.put(className, classLectureCount.getOrDefault(className, 0) + 1);

                    // Update subject count per class
                    subjectClassCount.put(className + "-" + subjectCode,
                            subjectClassCount.getOrDefault(className + "-" + subjectCode, 0) + 1);

                    Log.d("DEBUG", "Strictly Assigned Teacher: " + teacher + " to Subject: " + subjectCode + " in " + className + " at " + timeSlot);
                    return teacher;
                }
            }
        }

        // Balance assignment
        String selectedTeacher = null;
        int minLectureCount = Integer.MAX_VALUE;

        for (String teacher : teachers) {
            Set<String> assignedTeachersClass = teacherSchedule.get(className).get(timeSlot);
            Set<String> assignedTeachersGlobal = globalTeacherSchedule.get(timeSlot);

            if (!assignedTeachersClass.contains(teacher) && !assignedTeachersGlobal.contains(teacher)) {
                int lectureCount = classLectureCount.getOrDefault(className, 0);
                if (lectureCount < minLectureCount) {
                    minLectureCount = lectureCount;
                    selectedTeacher = teacher;
                }
            }
        }

        if (selectedTeacher != null) {
            teacherSchedule.get(className).get(timeSlot).add(selectedTeacher);
            globalTeacherSchedule.get(timeSlot).add(selectedTeacher);
            classLectureCount.put(className, classLectureCount.getOrDefault(className, 0) + 1);

            // Update subject count per class
            subjectClassCount.put(className + "-" + subjectCode,
                    subjectClassCount.getOrDefault(className + "-" + subjectCode, 0) + 1);

            Log.d("DEBUG", "Balanced Assigned Teacher: " + selectedTeacher + " to Subject: " + subjectCode + " in " + className + " at " + timeSlot);
            return selectedTeacher;
        }

        // No teacher assigned
        classNoTeacherCount.put(className, classNoTeacherCount.getOrDefault(className, 0) + 1);
        Log.d("DEBUG", "No teacher assigned for Subject: " + subjectCode + " in Class: " + className + " at " + timeSlot);
        return null;
    }



//    private String assignTeacher(String className, String subjectCode,
//                                 Map<String, List<String>> subjectTeachers,
//                                 Map<String, Map<String, Set<String>>> teacherSchedule,
//                                 Map<String, Set<String>> globalTeacherSchedule,
//                                 Map<String, Integer> classLectureCount,
//                                 Map<String, Integer> classNoTeacherCount,
//                                 String day, int hour) {
//
//        // Get the list of teachers available for the subject
//        List<String> teachers = subjectTeachers.get(subjectCode);
//        if (teachers == null || teachers.isEmpty()) {
//            Log.d("DEBUG", "No teachers available for Subject: " + subjectCode);
//            // Increment the "No Teacher" counter for this class
//            classNoTeacherCount.put(className, classNoTeacherCount.getOrDefault(className, 0) + 1);
//            return null; // No teachers are available for this subject
//        }
//
//        String timeSlot = day + "-" + hour;
//
//        // Initialize schedules for the class and globally
//        teacherSchedule.putIfAbsent(className, new HashMap<>());
//        teacherSchedule.get(className).putIfAbsent(timeSlot, new HashSet<>());
//        globalTeacherSchedule.putIfAbsent(timeSlot, new HashSet<>());
//
//        for (String teacher : teachers) {
//            // Check if the teacher is assigned to the current class for this subject
//            String assignedClass = dbHelper.getAssignedClassForTeacherAndSubject(teacher, subjectCode);
//            boolean isExplicitlyAssigned = className.equals(assignedClass);
//
//            // Check if the teacher is free for the current slot
//            Set<String> assignedTeachersClass = teacherSchedule.get(className).get(timeSlot);
//            Set<String> assignedTeachersGlobal = globalTeacherSchedule.get(timeSlot);
//
//            if (!assignedTeachersClass.contains(teacher) && !assignedTeachersGlobal.contains(teacher)) {
//                if (isExplicitlyAssigned) {
//                    // Strictly assign teacher
//                    assignedTeachersClass.add(teacher);
//                    assignedTeachersGlobal.add(teacher);
//
//                    // Update lecture count for the class
//                    classLectureCount.put(className, classLectureCount.getOrDefault(className, 0) + 1);
//                    Log.d("DEBUG", "Strictly Assigned Teacher: " + teacher + " to Subject: " + subjectCode + " in " + className + " at " + timeSlot);
//                    return teacher;
//                }
//            }
//        }
//
//        // Fallback logic: Distribute teachers and lectures evenly
//        String selectedTeacher = null;
//        int minLectureCount = Integer.MAX_VALUE;
//
//        for (String teacher : teachers) {
//            Set<String> assignedTeachersClass = teacherSchedule.get(className).get(timeSlot);
//            Set<String> assignedTeachersGlobal = globalTeacherSchedule.get(timeSlot);
//
//            if (!assignedTeachersClass.contains(teacher) && !assignedTeachersGlobal.contains(teacher)) {
//                // Check lecture distribution across classes
//                int lectureCount = classLectureCount.getOrDefault(className, 0);
//
//                if (lectureCount < minLectureCount) {
//                    minLectureCount = lectureCount;
//                    selectedTeacher = teacher;
//                }
//            }
//        }
//
//        if (selectedTeacher != null) {
//            teacherSchedule.get(className).get(timeSlot).add(selectedTeacher);
//            globalTeacherSchedule.get(timeSlot).add(selectedTeacher);
//            classLectureCount.put(className, classLectureCount.getOrDefault(className, 0) + 1);
//
//            Log.d("DEBUG", "Balanced Assigned Teacher: " + selectedTeacher + " to Subject: " + subjectCode + " in " + className + " at " + timeSlot);
//            return selectedTeacher;
//        }
//
//        // If no teacher can be assigned, increment "No Teacher" counter
//        classNoTeacherCount.put(className, classNoTeacherCount.getOrDefault(className, 0) + 1);
//        Log.d("DEBUG", "No teacher assigned for Subject: " + subjectCode + " in Class: " + className + " at " + timeSlot);
//        return null;
//    }



    /**
     * Fetch subjects and their assigned teachers from TeacherSubjectDB
     */
    private Map<String, List<String>> fetchSubjectTeachers() {
        Map<String, List<String>> subjectTeachers = new HashMap<>();
        Cursor cursor = dbHelper.viewTable(DatabaseHelper.TABLE_TEACHER_SUBJECT);

        while (cursor.moveToNext()) {
            String subject = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TEACHER_SUBJECT_SUBJECT_CODE));
            String teacher = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TEACHER_SUBJECT_TEACHER_EMAIL));

            subjectTeachers.putIfAbsent(subject, new ArrayList<>());
            subjectTeachers.get(subject).add(teacher);
        }

        cursor.close();
        Log.d("DEBUG", "Fetched Subject-Teacher Assignments: " + subjectTeachers);
        return subjectTeachers;
    }


    /**
     * Fetch subjects with their credits
     */
    private Map<String, Integer> fetchSubjectsWithCredits() {
        Map<String, Integer> subjects = new HashMap<>();
        Cursor subjectCursor = dbHelper.viewTable(DatabaseHelper.TABLE_SUBJECT);

        while (subjectCursor.moveToNext()) {
            String subjectName = subjectCursor.getString(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_NAME));
            int credits = subjectCursor.getInt(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_CREDITS));
            subjects.put(subjectName, credits);
        }

        subjectCursor.close();
        Log.d("DEBUG", "Fetched Subject Credits: " + subjects);
        return subjects;
    }



    // ----------------PDF Saving



    private void sharePDF(Uri fileUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission to apps to read the file
        startActivity(Intent.createChooser(shareIntent, "Share Timetable PDF"));
    }


    private void generatePDF() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Generating PDF...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());

                TableLayout[] timetables = {timetableTable1, timetableTable2, timetableTable3};

                String fileName = "PlanPal_" + currentDateAndTime + "_timetables.pdf";
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
                if (uri == null) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        showToast("Error creating file URI for PDF");
                    });
                    return;
                }

                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream == null) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        showToast("Error opening output stream for PDF");
                    });
                    return;
                }

                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDoc = new PdfDocument(writer);
                PageSize pageSize = PageSize.A4.rotate();
                pdfDoc.setDefaultPageSize(pageSize);
                Document document = new Document(pdfDoc, pageSize);
                document.setMargins(0, 0, 0, 0);

                for (int tableIndex = 0; tableIndex < timetables.length; tableIndex++) {
                    document.add(new Paragraph("JSPM's\nJayawantrao Sawant Polytechnic\nEven/ Odd Semester")
                            .setBold()
                            .setFontSize(20)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setHorizontalAlignment(HorizontalAlignment.CENTER));

                    float[] columnWidths = new float[7];
                    for (int i = 0; i < columnWidths.length; i++) {
                        columnWidths[i] = pageSize.getWidth() / 7f;
                    }

                    Table table = new Table(columnWidths);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    table.setWidth(pageSize.getWidth());

                    table.addCell(new Cell().add(new Paragraph("Day").setBold()).setTextAlignment(TextAlignment.CENTER));
                    String[] timeSlots = {"8:30 to 9:30", "9:30 to 10:30", "11:00 to 12:00", "12:00 to 1:00", "1:45 to 2:45", "2:45 to 3:45"};
                    for (String timeSlot : timeSlots) {
                        table.addCell(new Cell().add(new Paragraph(timeSlot).setBold()).setTextAlignment(TextAlignment.CENTER));
                    }

                    for (int i = 0; i < timetables[tableIndex].getChildCount(); i++) {
                        View view = timetables[tableIndex].getChildAt(i);
                        if (view instanceof TableRow) {
                            TableRow row = (TableRow) view;
                            TextView dayCell = (TextView) row.getChildAt(0);
                            table.addCell(new Cell().add(new Paragraph(dayCell.getText().toString()).setTextAlignment(TextAlignment.CENTER)));
                            for (int j = 1; j < row.getChildCount(); j++) {
                                TextView cellView = (TextView) row.getChildAt(j);
                                Cell pdfCell = new Cell().add(new Paragraph(cellView.getText().toString()).setTextAlignment(TextAlignment.CENTER));
                                if (cellView.getBackground() != null) {
                                    pdfCell.setBackgroundColor(ColorConstants.RED);
                                }
                                table.addCell(pdfCell);
                            }
                        }
                    }
                    document.add(table);

                    if (tableIndex < timetables.length - 1) {
                        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                    }
                }

                document.close();
                pdfDoc.close();
                writer.close();

                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    sharePDF(uri);
                    showToast("PDF saved in Downloads.");
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showToast("Error generating PDF!");
                });
            }
        }).start();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}