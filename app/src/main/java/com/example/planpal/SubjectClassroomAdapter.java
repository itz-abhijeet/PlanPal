package com.example.planpal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.planpal.models.SubjectClassroom;

import java.util.List;

public class SubjectClassroomAdapter extends RecyclerView.Adapter<SubjectClassroomAdapter.SubjectClassroomViewHolder> {
    private final List<SubjectClassroom> subjectClassroomList;
    private final OnRemoveClickListener onRemoveClickListener;

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);  // The position of the item being clicked
    }

    public SubjectClassroomAdapter(List<SubjectClassroom> subjectClassroomList, OnRemoveClickListener onRemoveClickListener) {
        this.subjectClassroomList = subjectClassroomList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @Override
    public SubjectClassroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_classroom, parent, false);
        return new SubjectClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectClassroomViewHolder holder, int position) {
        SubjectClassroom subjectClassroom = subjectClassroomList.get(position);
        holder.roomNoTextView.setText("Room: " + subjectClassroom.getRoomNo());
        holder.yearTextView.setText("Year: " + subjectClassroom.getYear());
        holder.subjectCodeTextView.setText("Subject: " + subjectClassroom.getSubjectCode());

        // Set the remove button click listener
        holder.removeButton.setOnClickListener(v -> onRemoveClickListener.onRemoveClick(position));
    }

    @Override
    public int getItemCount() {
        return subjectClassroomList.size();
    }

    public static class SubjectClassroomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNoTextView;
        TextView yearTextView;
        TextView subjectCodeTextView;
        Button removeButton;

        public SubjectClassroomViewHolder(View itemView) {
            super(itemView);
            roomNoTextView = itemView.findViewById(R.id.roomNoText);
            yearTextView = itemView.findViewById(R.id.yearText);
            subjectCodeTextView = itemView.findViewById(R.id.subjectCodeText);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
