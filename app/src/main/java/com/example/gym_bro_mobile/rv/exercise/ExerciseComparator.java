package com.example.gym_bro_mobile.rv;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.gym_bro_mobile.model.Exercise;

public class ExerciseComparator extends DiffUtil.ItemCallback<Exercise> {

    @Override
    public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
        return oldItem.equals(newItem);
    }
}

