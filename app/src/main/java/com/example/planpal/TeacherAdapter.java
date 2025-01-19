package com.example.planpal;

import com.example.planpal.models.Teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {
    private Context context;
    private List<Teacher> teacherList;
    private View.OnClickListener removeClickListener;

    public TeacherAdapter(Context context, List<Teacher> teacherList, View.OnClickListener removeClickListener) {
        this.context = context;
        this.teacherList = teacherList;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_teacher, parent, false);
        return new TeacherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.teacherName.setText(teacher.getName());
        holder.teacherEmail.setText(teacher.getEmail());

        // Set up the remove button click listener
        holder.removeButton.setOnClickListener(v -> {
            removeClickListener.onClick(v);
            // Handle further onClick or dialog
        });
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName;
        TextView teacherEmail;
        Button removeButton;

        public TeacherViewHolder(View view) {
            super(view);
            teacherName = view.findViewById(R.id.teacherName);
            teacherEmail = view.findViewById(R.id.teacherEmail);
            removeButton = view.findViewById(R.id.removeButton);
        }
    }
}
