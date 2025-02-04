package com.example.planpal;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.planpal.DatabaseHelper;

import java.util.Random;

public class GenerateTimetable extends AppCompatActivity {

    private TableLayout timetableTable;
    private Button generateTimetableButton;
    private DatabaseHelper dbHelper;
    TextView nameofInstitute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_timetable);


        nameofInstitute = findViewById(R.id.nameofInstitute);
        timetableTable = findViewById(R.id.timetableTable);
        generateTimetableButton = findViewById(R.id.generateTimetableButton);
        dbHelper = new DatabaseHelper(this);

        generateTimetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateTimetable();
            }
        });
    }

    private void generateTimetable() {
        clearTimetable();
        nameofInstitute.setVisibility(View.VISIBLE);

        // Array to hold day names
        String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT"};

        // Create header row for hour indices (1 to 6)
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // Add empty cell for the top-left corner
        TextView emptyCell = new TextView(this);
        emptyCell.setLayoutParams(new TableRow.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        headerRow.addView(emptyCell);

        // Add hour headers (1 to 6)
        for (int hour = 1; hour <= 6; hour++) {
            TextView hourCell = new TextView(this);
            hourCell.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            hourCell.setPadding(8, 8, 8, 8);
            hourCell.setTextSize(16);
            hourCell.setText(String.valueOf(hour));
            headerRow.addView(hourCell);
            hourCell.setTextColor(Color.BLACK);
        }

        // Add header row to the timetable table
        timetableTable.addView(headerRow);

        // Loop through each day (Mon to Fri)
        for (int dayIndex = 0; dayIndex < 6; dayIndex++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Add day name cell (Mon to Fri)
            TextView dayCell = new TextView(this);
            dayCell.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            dayCell.setPadding(8, 8, 8, 8);
            dayCell.setTextSize(16);
            dayCell.setText(days[dayIndex]);
            row.addView(dayCell);
            dayCell.setTextColor(Color.BLACK);

            // Loop through each hour (1 to 6)
            for (int hour = 1; hour <= 6; hour++) {
                TextView cell = new TextView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                cell.setPadding(8, 8, 8, 8);
                cell.setTextSize(16);
                cell.setText("Loading..."); // Placeholder text

                // Fetch and display a random subject for the current day and hour
                fetchRandomSubject(dayIndex, hour, cell);

                cell.setTextColor(Color.BLACK);
                row.addView(cell);
            }

            // Add row to the timetable table
            timetableTable.addView(row);
        }
    }

    private void fetchRandomSubject(final int dayIndex, final int hour, final TextView cell) {
        // Fetch all subjects from the database
        Cursor subjectCursor = dbHelper.viewTable(DatabaseHelper.TABLE_SUBJECT);

        if (subjectCursor != null && subjectCursor.getCount() > 0) {
            // Generate a random index
            int randomIndex = new Random().nextInt(subjectCursor.getCount());

            // Move the cursor to the random index
            subjectCursor.moveToPosition(randomIndex);

            // Get the subject name and code
            String subjectName = subjectCursor.getString(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_NAME));
            String subjectCode = subjectCursor.getString(subjectCursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT_CODE));


            // Display the subject name in the cell
            cell.setText(subjectName);

            // Save the timetable entry to the database
            saveTimetableEntry(dayIndex, hour, subjectCode);
        } else {
            cell.setText("No subjects found");
        }

        if (subjectCursor != null) {
            subjectCursor.close();
        }
    }

    private void saveTimetableEntry(int dayIndex, int hour, String subjectCode) {
        // Fetch a random classroom for the subject
        Cursor classroomCursor = dbHelper.viewTable(DatabaseHelper.TABLE_CLASSROOM);

        if (classroomCursor != null && classroomCursor.getCount() > 0) {
            int randomIndex = new Random().nextInt(classroomCursor.getCount());
            classroomCursor.moveToPosition(randomIndex);

            // Get the room number and year
            String roomNumber = classroomCursor.getString(classroomCursor.getColumnIndexOrThrow(DatabaseHelper.CLASSROOM_ROOM));
            String year = classroomCursor.getString(classroomCursor.getColumnIndexOrThrow(DatabaseHelper.CLASSROOM_YEAR));

            // Save the timetable entry to the database
            dbHelper.addTimetableEntry(dayIndex, hour, subjectCode, roomNumber, year);
        }

        if (classroomCursor != null) {
            classroomCursor.close();
        }
    }

    private void clearTimetable() {
        // Clear existing rows in timetableTable
        timetableTable.removeAllViews();
    }
}