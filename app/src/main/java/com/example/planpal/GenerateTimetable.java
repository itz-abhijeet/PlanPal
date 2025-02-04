package com.example.planpal;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class GenerateTimetable extends AppCompatActivity {

    private TableLayout timetableTable1, timetableTable2, timetableTable3;
    private Button generateTimetableButton;
    private DatabaseHelper dbHelper;
    TextView nameofInstitute, c1, c2, c3;

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

        c1 = findViewById(R.id.class1text);
        c2 = findViewById(R.id.class2text);
        c3 = findViewById(R.id.class3text);


        generateTimetableButton.setOnClickListener(v -> generateTimetables());
    }


    private void generateTimetables() {
        clearTimetables();
        nameofInstitute.setVisibility(View.VISIBLE);
        c1.setVisibility(View.VISIBLE);
        c2.setVisibility(View.VISIBLE);
        c3.setVisibility(View.VISIBLE);

        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT"};
        TableLayout[] timetables = {timetableTable1, timetableTable2, timetableTable3};

        // Fetch subjects once and shuffle them
        List<String> allSubjects = fetchAllSubjects();

        if (allSubjects.isEmpty()) {
            Toast.makeText(this,"Subjects doesn't exists",Toast.LENGTH_SHORT).show();
            return; // No subjects in DB
        }

        for (TableLayout table : timetables) {
            TableRow headerRow = createHeaderRow();
            table.addView(headerRow);

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

                // Shuffle a new subject list for each day to ensure variety
                List<String> dailySubjects = new ArrayList<>(allSubjects);
                Collections.shuffle(dailySubjects);

                for (int hour = 0; hour < 6; hour++) {
                    TextView cell = new TextView(this);
                    cell.setLayoutParams(new TableRow.LayoutParams(
                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
                    ));
                    cell.setPadding(8, 8, 8, 8);
                    cell.setTextSize(16);

                    String subject = dailySubjects.get(hour % dailySubjects.size());
                    cell.setText(subject);
                    cell.setTextColor(Color.BLACK);
                    row.addView(cell);
                }

                table.addView(row);
            }
        }
    }



//    private void generateTimetables() {
//        clearTimetables();
//        nameofInstitute.setVisibility(View.VISIBLE);
//
//        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT"};
//        TableLayout[] timetables = {timetableTable1, timetableTable2, timetableTable3};
//
//        // Fetch subjects from the database
//        List<String> allSubjects = fetchAllSubjects();
//
//        if (allSubjects.isEmpty()) {
//            return; // No subjects in DB
//        }
//
//        for (int i = 0; i < days.length; i++) { // Loop through days
//            String day = days[i];
//
//            TableRow[] rows = new TableRow[timetables.length];
//            List<HashSet<String>> slotTrackers = new ArrayList<>();
//
//            for (int slot = 0; slot < 6; slot++) {
//                slotTrackers.add(new HashSet<>()); // Create a tracker for each slot
//            }
//
//            for (int j = 0; j < timetables.length; j++) { // Loop through tables
//                TableLayout table = timetables[j];
//
//                // Create a new row for this day
//                TableRow row = new TableRow(this);
//                row.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.WRAP_CONTENT
//                ));
//
//                // Create the first column with the day name
//                if (j == 0) {
//                    TextView dayCell = new TextView(this);
//                    dayCell.setLayoutParams(new TableRow.LayoutParams(
//                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
//                    ));
//                    dayCell.setPadding(8, 8, 8, 8);
//                    dayCell.setTextSize(16);
//                    dayCell.setText(day);
//                    dayCell.setTextColor(Color.BLACK);
//                    row.addView(dayCell);
//                }
//
//                // Shuffle a new subject list for each day
//                List<String> dailySubjects = new ArrayList<>(allSubjects);
//                Collections.shuffle(dailySubjects);
//
//                for (int slot = 0; slot < 6; slot++) {
//                    TextView cell = new TextView(this);
//                    cell.setLayoutParams(new TableRow.LayoutParams(
//                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
//                    ));
//                    cell.setPadding(8, 8, 8, 8);
//                    cell.setTextSize(16);
//
//                    // Assign a subject
//                    String subject = dailySubjects.get(slot % dailySubjects.size());
//                    cell.setText(subject);
//                    cell.setTextColor(Color.BLACK);
//
//                    // Check for overlap
//                    if (!slotTrackers.get(slot).add(subject)) {
//                        // If subject already exists in this slot, it's an overlap
//                        cell.setBackgroundColor(Color.RED);
//                    }
//
//                    row.addView(cell);
//                }
//
//                table.addView(row);
//                rows[j] = row;
//            }
//        }
//    }



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
        headerRow.addView(emptyCell);

        for (int hour = 1; hour <= 6; hour++) {
            TextView hourCell = new TextView(this);
            hourCell.setLayoutParams(new TableRow.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
            ));
            hourCell.setPadding(8, 8, 8, 8);
            hourCell.setTextSize(16);
            hourCell.setText(String.valueOf(hour));
            hourCell.setTextColor(Color.BLACK);
            headerRow.addView(hourCell);
        }
        return headerRow;
    }

    private List<String> fetchAllSubjects() {
        List<String> subjects = new ArrayList<>();
        Cursor subjectCursor = dbHelper.viewTable(DatabaseHelper.TABLE_SUBJECT);

        while (subjectCursor.moveToNext()) {
            String subjectName = subjectCursor.getString(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_NAME));
            subjects.add(subjectName);
        }

        subjectCursor.close();
        return subjects;
    }

    private void clearTimetables() {
        timetableTable1.removeAllViews();
        timetableTable2.removeAllViews();
        timetableTable3.removeAllViews();
    }
}
