package com.example.planpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectTeacherAdapter extends RecyclerView.Adapter<SubjectTeacherAdapter.ViewHolder> {

    private Context context;
    private List<String> subjects;
    private List<String> teachers;
    private Map<String, String> selectedTeachers; // Mapping of Subject -> Selected Teacher

    public SubjectTeacherAdapter(Context context, List<String> subjects, List<String> teachers) {
        this.context = context;
        this.subjects = subjects;
        this.teachers = teachers;
        this.selectedTeachers = new HashMap<>();
    }

    public Map<String, String> getSelectedTeachers() {
        return selectedTeachers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject_teacher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String subject = subjects.get(position);
        holder.txtSubject.setText(subject);


        // Populate the Spinner with the list of teachers
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, teachers);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerTeacher.setAdapter(teacherAdapter);

        // Restore previously selected teacher if available
        if (selectedTeachers.containsKey(subject)) {
            int selectedIndex = teachers.indexOf(selectedTeachers.get(subject));
            if (selectedIndex != -1) {
                holder.spinnerTeacher.setSelection(selectedIndex);
            }
        }

        // Handle teacher selection
        holder.spinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int teacherPosition, long id) {
                selectedTeachers.put(subject, teachers.get(teacherPosition)); // Store selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubject;
        Spinner spinnerTeacher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject = itemView.findViewById(R.id.txtSubject);
            spinnerTeacher = itemView.findViewById(R.id.spinnerTeacher);
        }
    }
}
