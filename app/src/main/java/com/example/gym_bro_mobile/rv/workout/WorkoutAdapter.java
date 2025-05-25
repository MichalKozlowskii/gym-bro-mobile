package com.example.gym_bro_mobile.rv.workout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExerciseSet;
import com.example.gym_bro_mobile.model.Workout;

public class WorkoutAdapter extends ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder> {

    public interface OnWorkoutClickListener {
        void onClick(Workout workout);
    }

    public interface OnWorkoutActionListener {
        void onDeleteWorkout(Workout workout);
        void onAddSet(Workout workout, Exercise exercise);
        void onDeleteSet(Workout workout, Exercise exercise, ExerciseSet set);
    }

    private final OnWorkoutClickListener clickListener;
    private final OnWorkoutActionListener actionListener;

    public WorkoutAdapter(OnWorkoutClickListener clickListener, OnWorkoutActionListener actionListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
        this.actionListener = actionListener;
    }

    private static final DiffUtil.ItemCallback<Workout> DIFF_CALLBACK = new DiffUtil.ItemCallback<Workout>() {
        @Override
        public boolean areItemsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workout oldItem, @NonNull Workout newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = getItem(position);
        holder.bind(workout);
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName, workoutDate;
        LinearLayout exerciseContainer;
        ImageButton btnDeleteWorkout;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workoutPlanName);
            workoutDate = itemView.findViewById(R.id.workout_date);
            exerciseContainer = itemView.findViewById(R.id.exercise_container);
            btnDeleteWorkout = itemView.findViewById(R.id.btnDeleteWorkout);
        }

        void bind(Workout workout) {
            workoutName.setText(workout.getWorkoutPlan().getName());
            workoutDate.setText("Created: " + workout.getCreationDate());

            btnDeleteWorkout.setOnClickListener(v -> {
                if (actionListener != null) {
                    actionListener.onDeleteWorkout(workout);
                }
            });

            exerciseContainer.removeAllViews();

            for (Exercise exercise : workout.getWorkoutPlan().getExercises()) {
                View exerciseRow = LayoutInflater.from(itemView.getContext()).inflate(R.layout.rv_exercise_row, exerciseContainer, false);

                TextView exerciseName = exerciseRow.findViewById(R.id.exerciseName);
                Button btnAddSet = exerciseRow.findViewById(R.id.btnAddSet);
                LinearLayout setsContainer = exerciseRow.findViewById(R.id.sets_container);

                exerciseName.setText(exercise.getName());

                btnAddSet.setOnClickListener(v -> {
                    if (actionListener != null) {
                        actionListener.onAddSet(workout, exercise);
                    }
                });

                setsContainer.removeAllViews();

                if (workout.getExerciseSetMap().containsKey(exercise)) {
                    for (ExerciseSet set : workout.getExerciseSetMap().get(exercise)) {
                        View setRow = LayoutInflater.from(itemView.getContext()).inflate(R.layout.rv_set_row, setsContainer, false);

                        TextView tvSetInfo = setRow.findViewById(R.id.setInfo);
                        ImageButton btnDeleteSet = setRow.findViewById(R.id.btnDeleteSet);

                        String setInfo = "Reps: " + set.getReps() + ", Weight: " + Math.round(set.getWeight() * 100) / 100. + " kg";
                        tvSetInfo.setText(setInfo);

                        btnDeleteSet.setOnClickListener(v -> {
                            if (actionListener != null) {
                                actionListener.onDeleteSet(workout, exercise, set);
                            }
                        });

                        setsContainer.addView(setRow);
                    }
                }

                exerciseContainer.addView(exerciseRow);
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onClick(workout);
                }
            });
        }
    }
}
