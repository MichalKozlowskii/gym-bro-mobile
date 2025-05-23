package com.example.gym_bro_mobile.rv.workout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.example.gym_bro_mobile.model.Workout;

public class WorkoutComparator extends DiffUtil.ItemCallback<Workout> {
    @Override
    public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
        return oldItem.equals(newItem);
    }
}