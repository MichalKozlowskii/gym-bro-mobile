package com.example.gym_bro_mobile.rv.workoutplan;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.gym_bro_mobile.databinding.RvWorkoutplanBinding;
import com.example.gym_bro_mobile.model.WorkoutPlan;

public class WorkoutPlanAdapter extends ListAdapter<WorkoutPlan, WorkoutPlanViewHolder> {
    private final OnWorkoutPlanClickListener listener;

    public WorkoutPlanAdapter(OnWorkoutPlanClickListener listener) {
        super(new WorkoutPlanComparator());
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RvWorkoutplanBinding binding = RvWorkoutplanBinding.inflate(inflater, parent, false);
        return new WorkoutPlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutPlanViewHolder holder, int position) {
        WorkoutPlan plan = getItem(position);
        holder.bind(plan, listener);
    }
}
