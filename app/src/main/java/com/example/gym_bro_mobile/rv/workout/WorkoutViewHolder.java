package com.example.gym_bro_mobile.rv.workout;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gym_bro_mobile.databinding.RvWorkoutBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExerciseSet;
import com.example.gym_bro_mobile.model.Workout;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    private final RvWorkoutBinding binding;

    public WorkoutViewHolder(RvWorkoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Workout workout, OnWorkoutClickListener listener) {
        binding.workoutPlanName.setText(workout.getWorkoutPlan().getName());
        binding.workoutDate.setText("Created: " + workout.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        binding.exerciseContainer.removeAllViews();

        for (Map.Entry<Exercise, List<ExerciseSet>> entry : workout.getExerciseSetMap().entrySet()) {
            Exercise exercise = entry.getKey();
            List<ExerciseSet> sets = entry.getValue();

            TextView exerciseTitle = new TextView(binding.getRoot().getContext());
            exerciseTitle.setText(exercise.getName());
            exerciseTitle.setTextSize(16f);
            exerciseTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            binding.exerciseContainer.addView(exerciseTitle);

            for (ExerciseSet set : sets) {
                TextView setView = new TextView(binding.getRoot().getContext());
                String text = "Reps: " + set.getReps() + ", Weight: " + set.getWeight();
                setView.setText(text);
                setView.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), android.R.color.darker_gray));
                setView.setTextSize(14f);
                binding.exerciseContainer.addView(setView);
            }
        }

        binding.workoutRoot.setOnClickListener(v -> listener.onWorkoutClick(workout));
    }

    public static WorkoutViewHolder create(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RvWorkoutBinding binding = RvWorkoutBinding.inflate(inflater, parent, false);
        return new WorkoutViewHolder(binding);
    }
}

