package com.example.gym_bro_mobile.rv.exercise;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.gym_bro_mobile.databinding.RvExerciseBinding;
import com.example.gym_bro_mobile.model.Exercise;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseViewHolder> {
    private final OnExerciseClickListener listener;

    public ExerciseAdapter(OnExerciseClickListener listener) {
        super(new ExerciseComparator());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseViewHolder(RvExerciseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}

