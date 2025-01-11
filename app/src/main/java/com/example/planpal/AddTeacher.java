package com.example.planpal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTeacher extends AppCompatActivity {

    TextView teacherName, teacherSubject;
    Button addTeacher;
    DatabaseHelper databaseHelper;

    final String[] teacherNameStorage = {""};
    final String[] teacherSubStorage = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher);

        databaseHelper = new DatabaseHelper(this);
        teacherName = findViewById(R.id.addTeacher_name);
        teacherSubject = findViewById(R.id.addTeacher_sub);
        addTeacher = findViewById(R.id.addTeacher_Add);

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                teacherNameStorage[0] = teacherName.getText().toString();
                teacherSubStorage[0] = teacherSubject.getText().toString();

                if(teacherNameStorage[0].isEmpty() || teacherSubStorage[0].isEmpty()){
                    Toast.makeText(AddTeacher.this, "Fill the fields properly", Toast.LENGTH_SHORT).show();
                }

                else{
                    // Now you can use the classNo, selectedYearValue, and classtype for further processing
                    Toast.makeText(AddTeacher.this, "Teacher: "+ teacherName +" added", Toast.LENGTH_SHORT).show();
                    databaseHelper.insertTeacher(teacherName.toString(),teacherSubject.toString());
                    teacherName.setText("");
                    teacherSubject.setText("");
                }

            }
        });

    }
}