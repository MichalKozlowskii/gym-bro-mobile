package com.example.gym_bro_mobile.rv.workoutplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.gym_bro_mobile.model.WorkoutPlan;

public class WorkoutPlanComparator extends DiffUtil.ItemCallback<WorkoutPlan> {
    @Override
    public boolean areItemsTheSame(@NonNull WorkoutPlan oldItem, @NonNull WorkoutPlan newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull WorkoutPlan oldItem, @NonNull WorkoutPlan newItem) {
        return oldItem.equals(newItem);
    }
}
