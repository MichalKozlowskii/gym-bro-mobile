package com.example.gym_bro_mobile.rv.workoutplan;
import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gym_bro_mobile.databinding.RvWorkoutplanBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.SetsReps;
import com.example.gym_bro_mobile.model.WorkoutPlan;

public class WorkoutPlanViewHolder extends RecyclerView.ViewHolder {
    private final RvWorkoutplanBinding binding;

    public WorkoutPlanViewHolder(@NonNull RvWorkoutplanBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(WorkoutPlan plan, OnWorkoutPlanClickListener listener) {
        binding.planName.setText(plan.getName());
        binding.planCreationDate.setText("Created: " + plan.getCreationDate().toLocalDate().toString());
        binding.llExercisesContainer.removeAllViews();

        for (int i = 0; i < plan.getExercises().size(); i++) {
            Exercise exercise = plan.getExercises().get(i);
            SetsReps sr = plan.getSetsReps().get(i);

            TextView tv = new TextView(binding.getRoot().getContext());
            tv.setText(exercise.getName() + " — " + sr.getSets() + "x" + sr.getReps());
            tv.setTextSize(14f);
            tv.setTextColor(Color.BLACK);

            binding.llExercisesContainer.addView(tv);
        }

        binding.getRoot().setOnClickListener(v -> listener.onClick(plan));
    }
}