package com.example.planpal;

import com.example.planpal.models.Classroom;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RemoveClassroom extends AppCompatActivity {
    private RecyclerView rvClassrooms;
    private TextView tvEmpty;
    private ClassroomAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_classroom);

        rvClassrooms = findViewById(R.id.rv_classrooms);
        tvEmpty = findViewById(R.id.tv_empty);

        databaseHelper = new DatabaseHelper(this);

        List<Classroom> classrooms = fetchClassroomsFromDatabase();
        if (classrooms.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvClassrooms.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvClassrooms.setVisibility(View.VISIBLE);
            adapter = new ClassroomAdapter(this, classrooms, this::showConfirmationDialog);
            rvClassrooms.setLayoutManager(new LinearLayoutManager(this));
            rvClassrooms.setAdapter(adapter);
        }
    }

    private List<Classroom> fetchClassroomsFromDatabase() {
        List<Classroom> classrooms = new ArrayList<>();
        Cursor cursor = databaseHelper.viewTable("ClassroomDB");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room_number"));
                String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                classrooms.add(new Classroom(room, year));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return classrooms;
    }

    private void showConfirmationDialog(Classroom classroom) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Removal")
                .setMessage("Are you sure you want to remove this classroom?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean isSuccess = databaseHelper.removeClassroom(classroom.getRoomNumber(), classroom.getYear());
                    if (isSuccess) {
                        recreate(); // Refresh the activity
                    } else {
                        showErrorDialog();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Failed to remove the classroom.")
                .setPositiveButton("OK", null)
                .show();
    }
}
