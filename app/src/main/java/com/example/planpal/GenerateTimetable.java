package com.example.planpal;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.colors.ColorConstants;


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


    private void generateTimetables() {
        clearTimetables();
        nameofInstitute.setVisibility(View.VISIBLE);
        c1.setVisibility(View.VISIBLE);
        c2.setVisibility(View.VISIBLE);
        c3.setVisibility(View.VISIBLE);
        savePdfButton.setVisibility(View.VISIBLE);

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


    // ----------------PDF Saving


//    private void sharePDF(Uri fileUri) {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("application/pdf");
//        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);  // Add the PDF file URI
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  // Grant permission to apps to read the file
//
//        // Start the share intent to allow user to choose an app to share the file
//        startActivity(Intent.createChooser(shareIntent, "Share Timetable PDF"));
//    }

    private void sharePDF(Uri fileUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission to apps to read the file
        startActivity(Intent.createChooser(shareIntent, "Share Timetable PDF"));
    }

    private void generatePDF() {
        // Check storage permission for Android 9 (API level 28) and below
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // For older Android versions, check WRITE_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());

            // Generate the file name with "PlanPal" and the current date and time
            String fileName = "PlanPal_" + currentDateAndTime + ".pdf";
            // For Android 10 and above, use MediaStore API to save in the Downloads folder
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName); // File name
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            // Get the content resolver and insert the new file in Downloads
            //Uri uri = FileProvider.getUriForFile(this, "com.example.planpal.fileprovider", pdfFile);
            Uri uri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);

            // Write to the OutputStream of the URI
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add Title to PDF
            document.add(new Paragraph("College Timetable").setBold().setFontSize(20));

            // Array of TableLayouts for all 3 timetables
            TableLayout[] timetables = {timetableTable1, timetableTable2, timetableTable3};

            // Loop through each timetable (timetableTable1, timetableTable2, timetableTable3)
            for (int tableIndex = 0; tableIndex < timetables.length; tableIndex++) {
                TableLayout timetable = timetables[tableIndex];

                // Add header for each timetable
                document.add(new Paragraph("Timetable " + (tableIndex + 1)).setBold().setFontSize(16));

                // Create table with 7 columns (Day + 6 Slots)
                float[] columnWidths = {100, 80, 80, 80, 80, 80, 80};
                Table table = new Table(columnWidths);

                // Add header row
                table.addCell(new Cell().add(new Paragraph("Day").setBold()));
                for (int i = 1; i <= 6; i++) {
                    table.addCell(new Cell().add(new Paragraph("Slot " + i).setBold()));
                }

                // Loop through each row of the timetable (representing a day)
                for (int i = 0; i < timetable.getChildCount(); i++) {
                    View view = timetable.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;

                        // Add day name (first cell) to the PDF
                        TextView dayCell = (TextView) row.getChildAt(0);
                        table.addCell(new Cell().add(new Paragraph(dayCell.getText().toString())));

                        // Add the subjects for each slot
                        for (int j = 1; j < row.getChildCount(); j++) {
                            TextView cellView = (TextView) row.getChildAt(j);
                            Cell pdfCell = new Cell().add(new Paragraph(cellView.getText().toString()));

                            // Highlight overlaps in red
                            if (cellView.getBackground() != null) {
                                pdfCell.setBackgroundColor(ColorConstants.RED);
                            }
                            table.addCell(pdfCell);
                        }
                    }
                }

                // Add the table to the PDF document
                document.add(table);
            }

            // Close the document after writing all tables
            document.close();

            // Call share functionality after generating the PDF
            sharePDF(uri);

            showToast("PDF saved in Downloads: " + uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Error generating PDF!");
        }
    }


    // Helper function to show toast messages
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }



//    // Helper function to get TableRow for a specific day
//    private TableRow getRowForDay(String day) {
//        for (int i = 0; i < timetableTable1.getChildCount(); i++) {
//            View view = timetableTable1.getChildAt(i);
//            if (view instanceof TableRow) {
//                TableRow row = (TableRow) view;
//                TextView firstCell = (TextView) row.getChildAt(0);
//                if (firstCell.getText().toString().equals(day)) {
//                    return row;
//                }
//            }
//        }
//        return null;
//    }






}
