package com.example.planpal;

import com.example.planpal.models.TeacherSubject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeacherSubjectAdapter extends RecyclerView.Adapter<TeacherSubjectAdapter.ViewHolder> {
    private Context context;
    private List<TeacherSubject> teacherSubjectList;
    private OnRemoveClickListener clickListener;

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    public TeacherSubjectAdapter(Context context, List<TeacherSubject> teacherSubjectList, OnRemoveClickListener clickListener) {
        this.context = context;
        this.teacherSubjectList = teacherSubjectList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeacherSubject teacherSubject = teacherSubjectList.get(position);
        holder.teacherSubjectInfo.setText("Teacher: " + teacherSubject.getTeacherEmail() + "\nSubject: " + teacherSubject.getSubjectCode());
        holder.removeButton.setOnClickListener(v -> clickListener.onRemoveClick(position));
    }

    @Override
    public int getItemCount() {
        return teacherSubjectList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherSubjectInfo;
        Button removeButton;

        ViewHolder(View itemView) {
            super(itemView);
            teacherSubjectInfo = itemView.findViewById(R.id.teacherSubjectInfo);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
