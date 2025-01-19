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
        holder.subjectCode.setText(subject.getCode());

        holder.removeButton.setOnClickListener(v -> {
            removeClickListener.onClick(v);
            // Handle further onClick or dialog
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView subjectCode;
        Button removeButton;

        public SubjectViewHolder(View view) {
            super(view);
            subjectName = view.findViewById(R.id.subjectName);
            subjectCode = view.findViewById(R.id.subjectCode);
            removeButton = view.findViewById(R.id.removeButton);
        }
    }
}

