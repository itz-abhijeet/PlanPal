package com.example.planpal;

import com.example.planpal.models.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    private Context context;
    private List<Subject> subjectList;
    private View.OnClickListener removeClickListener;

    public SubjectAdapter(Context context, List<Subject> subjectList, View.OnClickListener removeClickListener) {
        this.context = context;
        this.subjectList = subjectList;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());
        holder.subjectCode.setText("Subject Code: " + subject.getCode());
        holder.subjectCredits.setText("Number Of Slots: " + subject.getCredits());

        holder.removeButton.setOnClickListener(v -> removeClickListener.onClick(v));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, subjectCode, subjectCredits;
        Button removeButton;

        public SubjectViewHolder(View view) {
            super(view);
            subjectName = view.findViewById(R.id.subjectName);
            subjectCode = view.findViewById(R.id.subjectCode);
            subjectCredits = view.findViewById(R.id.subjectCredits); // Add this in your layout
            removeButton = view.findViewById(R.id.removeButton);
        }
    }
}
