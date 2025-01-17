package com.example.planpal;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddClassroom extends AppCompatActivity {

    Spinner yearSpinner;
    EditText classNo, capacity, courseCode;
    Button addClassroom;
    RadioGroup classRg;
    RadioButton classRb;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_classroom);

        databaseHelper = new DatabaseHelper(this);
        classRg = findViewById(R.id.addClassroom_radioGroup);
        yearSpinner = findViewById(R.id.yearSpinner);
        classNo = findViewById(R.id.addClassroom_no);
        capacity = findViewById(R.id.addClassroom_capacity);
        courseCode = findViewById(R.id.addClassroom_courseCode);
        addClassroom = findViewById(R.id.addClassroom_Add);

        // Year options for the spinner
        String[] yearOptions = {"First Year", "Second Year", "Third Year"};

        // Create an ArrayAdapter for the Spinner
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


        // Apply the adapter to the spinner
        yearSpinner.setAdapter(adapter);

        // Set listener for spinner selection
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // No additional actions are needed since selected year is directly fetched later
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle the case where no item is selected
            }
        });

        addClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String roomNumber = classNo.getText().toString().trim();
                String selectedYear = yearSpinner.getSelectedItem().toString();
                String classCapacity = capacity.getText().toString().trim();
                String course = courseCode.getText().toString().trim();

                // Get selected type from RadioGroup
                int selectedRadioButtonId = classRg.getCheckedRadioButtonId();
                String classType;
                if (selectedRadioButtonId != -1) {
                    classRb = findViewById(selectedRadioButtonId);
                    classType = classRb.getText().toString();
                } else {
                    classType = ""; // No type selected
                }

                // Validate inputs
                if (roomNumber.isEmpty() || classCapacity.isEmpty() || course.isEmpty() || classType.isEmpty()) {
                    Toast.makeText(AddClassroom.this, "Fill all fields properly", Toast.LENGTH_SHORT).show();
                    return;
                }

                int capacityValue;
                try {
                    capacityValue = Integer.parseInt(classCapacity);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddClassroom.this, "Capacity must be a number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert into database
                boolean isAdded = databaseHelper.addClassroom(roomNumber, selectedYear, capacityValue, course, classType);
                if (isAdded) {
                    Toast.makeText(AddClassroom.this, "Classroom added successfully", Toast.LENGTH_SHORT).show();

                    // Clear input fields
                    classNo.setText("");
                    capacity.setText("");
                    courseCode.setText("");
                    classRg.clearCheck();
                    yearSpinner.setSelection(0);
                } else {
                    Toast.makeText(AddClassroom.this, "Error adding classroom. Check for duplicate entries.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
