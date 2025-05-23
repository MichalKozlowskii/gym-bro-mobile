package com.example.gym_bro_mobile.rv.workout;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import com.example.gym_bro_mobile.model.Workout;

public class WorkoutAdapter extends ListAdapter<Workout, WorkoutViewHolder> {
    private final OnWorkoutClickListener listener;

    public WorkoutAdapter(OnWorkoutClickListener listener) {
        super(new WorkoutComparator());
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WorkoutViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }
}
