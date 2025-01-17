package com.example.planpal;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddSubjectClassroom extends AppCompatActivity {

    private Spinner yearSpinner;
    private EditText etRoomNumber, etSubjectCode;
    private Button btnAddSubjectClassroom;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subject_classroom);

        databaseHelper = new DatabaseHelper(this);

        // Reference UI elements
        yearSpinner = findViewById(R.id.yearSpinner);
        etRoomNumber = findViewById(R.id.addsubjectClassroom_no);
        etSubjectCode = findViewById(R.id.addsubjectClassroom_subjectCode);
        btnAddSubjectClassroom = findViewById(R.id.addsubjectClassroom_Add);

        // Year options for spinner
        String[] yearOptions = {"First Year", "Second Year", "Third Year"};

        // Adapter for spinner with custom view styles
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearOptions) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                CheckedTextView textView = (CheckedTextView) view;
                textView.setTextColor(getResources().getColor(R.color.black));
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

        // Apply adapter to spinner
        yearSpinner.setAdapter(adapter);

        // Handle spinner item selection
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // You can fetch the selected year directly via yearSpinner.getSelectedItem().toString()
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle unselected state
            }
        });

        // Handle "Add" button click
        btnAddSubjectClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubjectClassroom();
            }
        });
    }

    private void addSubjectClassroom() {
        // Fetch inputs
        String roomNumber = etRoomNumber.getText().toString().trim();
        String subjectCode = etSubjectCode.getText().toString().trim();
        String selectedYear = yearSpinner.getSelectedItem().toString();

        // Validate inputs
        if (roomNumber.isEmpty() || subjectCode.isEmpty() || selectedYear.equals("Select Year")) {
            Toast.makeText(AddSubjectClassroom.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert into the database
        boolean isInserted = databaseHelper.addSubjectClassroom(roomNumber, selectedYear, subjectCode);

        if (isInserted) {
            Toast.makeText(AddSubjectClassroom.this, "Subject-Classroom mapping added successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
        } else {
            Toast.makeText(AddSubjectClassroom.this, "Error adding mapping. Check for duplicates.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        etRoomNumber.setText("");
        etSubjectCode.setText("");
        yearSpinner.setSelection(0); // Reset spinner to default
    }
}
