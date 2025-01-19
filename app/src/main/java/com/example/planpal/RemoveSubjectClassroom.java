package com.example.planpal;

import com.example.planpal.models.SubjectClassroom;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RemoveSubjectClassroom extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubjectClassroomAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<SubjectClassroom> subjectClassroomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remove_subject_classroom);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize the RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load subject classrooms from database
        loadSubjectClassrooms();

        // Initialize the adapter and set it to the RecyclerView
        adapter = new SubjectClassroomAdapter(subjectClassroomList, this::showConfirmationDialog);
        recyclerView.setAdapter(adapter);
    }

    private void loadSubjectClassrooms() {
        Cursor cursor = dbHelper.viewTable("SubjectClassroomDB");
        subjectClassroomList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get values from cursor and add to the list
                String roomNo = cursor.getString(cursor.getColumnIndexOrThrow("room_number"));
                String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                String subjectCode = cursor.getString(cursor.getColumnIndexOrThrow("subject_code"));
                subjectClassroomList.add(new SubjectClassroom(roomNo, year, subjectCode));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void showConfirmationDialog(int position) {
        // Show confirmation dialog
        SubjectClassroom subjectClassroom = subjectClassroomList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Subject Assignment")
                .setMessage("Are you sure you want to remove Subject Code: "
                        + subjectClassroom.getSubjectCode()
                        + " from Room: " + subjectClassroom.getRoomNo()
                        + ", Year: " + subjectClassroom.getYear() + "?")
                .setPositiveButton("Yes", (dialog, which) -> removeSubjectClassroom(subjectClassroom, position))
                .setNegativeButton("No", null)
                .show();
    }

    private void removeSubjectClassroom(SubjectClassroom subjectClassroom, int position) {
        // Remove subject classroom from database and list
        boolean removed = dbHelper.removeSubjectClassroom(subjectClassroom.getRoomNo(),
                subjectClassroom.getYear(),
                subjectClassroom.getSubjectCode());

        if (removed) {
            subjectClassroomList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(this, "Subject assignment removed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to remove subject assignment", Toast.LENGTH_SHORT).show();
        }
    }
}
