package com.example.planpal;

import com.example.planpal.models.Classroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder> {
    private final Context context;
    private final List<Classroom> classrooms;
    private final OnClassroomRemoveListener listener;

    public interface OnClassroomRemoveListener {
        void onRemove(Classroom classroom);
    }

    public ClassroomAdapter(Context context, List<Classroom> classrooms, OnClassroomRemoveListener listener) {
        this.context = context;
        this.classrooms = classrooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classroom, parent, false);
        return new ClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomViewHolder holder, int position) {
        Classroom classroom = classrooms.get(position);
        holder.tvRoomNumber.setText("Room: " + classroom.getRoomNumber());
        holder.tvYear.setText("Year: " + classroom.getYear());
        holder.btnRemove.setOnClickListener(v -> listener.onRemove(classroom));
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    public static class ClassroomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomNumber, tvYear;
        Button btnRemove;

        public ClassroomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomNumber = itemView.findViewById(R.id.tv_room_number);
            tvYear = itemView.findViewById(R.id.tv_year);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
