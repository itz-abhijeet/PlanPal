package com.example.planpal;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AddClassroom extends AppCompatActivity {

    Spinner yearSpinner;
    TextView className;
    Button addCLass;
    RadioGroup classRg;
    RadioButton classRb;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_classroom);

        databaseHelper = new DatabaseHelper(this);
        classRg = findViewById(R.id.addClassroom_radioGroup); // Assuming you have a RadioGroup with id `classRg`
        yearSpinner = findViewById(R.id.yearSpinner);
        className = findViewById(R.id.addClassroom_no);
        addCLass = findViewById(R.id.addClassroom_Add);

        final String[] classNo = {""};
        final String[] selectedYear = {""};
        final String[] classtype = {""};

        String[] yearOptions = {"First Year", "Second Year", "Third Year"};

        // Create an ArrayAdapter using the custom CheckedTextView layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearOptions) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                CheckedTextView textView = (CheckedTextView) view;
                textView.setTextColor(getResources().getColor(R.color.black)); // Ensure text color is black
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckedTextView textView = (CheckedTextView) view;
                textView.setTextColor(getResources().getColor(R.color.black)); // Ensure text color is black
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        yearSpinner.setAdapter(adapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle the case where no item is selected
            }
        });

        addCLass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered class name from the TextView
                classNo[0] = className.getText().toString(); // Correct way to get text from EditText

                // Get the selected year from Spinner
                String selectedYearValue = selectedYear[0]; // This will contain the selected item in Spinner

                // Get the selected class type from RadioGroup
                int selectedRadioButtonId = classRg.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    classRb = findViewById(selectedRadioButtonId); // Get the selected RadioButton
                    classtype[0] = classRb.getText().toString();  // Get the text of the selected RadioButton
                } else {
                    classtype[0] = "No class type selected"; // Handle case when no radio button is selected
                }

                if(classNo[0].isEmpty() || selectedYear[0].isEmpty() || classtype[0].isEmpty()){
                    Toast.makeText(AddClassroom.this, "Fill the fields properly", Toast.LENGTH_SHORT).show();
                }

                else{
                    // Now you can use the classNo, selectedYearValue, and classtype for further processing
                    Toast.makeText(AddClassroom.this, "Class: " + classNo[0] + "\nYear: " + selectedYearValue + "\nType: " + classtype[0], Toast.LENGTH_SHORT).show();
                    databaseHelper.insertClassroom(classNo[0], selectedYear[0], classtype[0]);
                    className.setText("");
                }

            }
        });

    }
}
