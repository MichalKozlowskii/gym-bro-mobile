package com.example.gym_bro_mobile.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.databinding.DialogNewSetBinding;
import com.example.gym_bro_mobile.databinding.FragmentWorkoutsBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExerciseSet;
import com.example.gym_bro_mobile.model.Workout;
import com.example.gym_bro_mobile.rv.workout.WorkoutAdapter;
import com.example.gym_bro_mobile.viewmodel.WorkoutsViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutsFragment extends Fragment {

    private FragmentWorkoutsBinding binding;
    private WorkoutsViewModel viewModel;
    private WorkoutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvWorkouts.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new WorkoutAdapter(
                workout -> {
                    // Handle workout click if needed
                },
                new WorkoutAdapter.OnWorkoutActionListener() {
                    @Override
                    public void onDeleteWorkout(Workout workout) {
                        viewModel.deleteWorkout(workout.getId(), view);
                    }

                    @Override
                    public void onAddSet(Workout workout, Exercise exercise) {
                        LayoutInflater inflater = getLayoutInflater();
                        DialogNewSetBinding binding = DialogNewSetBinding.inflate(inflater);

                        binding.exerciseName.setText(exercise.getName());

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setView(binding.getRoot());

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        binding.dialogButton.setOnClickListener(view -> {
                            int reps = Integer.parseInt(binding.reps.getText().toString().trim());
                            float weight = Float.parseFloat(binding.weight.getText().toString().trim());

                            viewModel.addSet(workout.getId(), exercise.getId(), reps, weight, view);

                            dialog.dismiss();
                        });
                    }

                    @Override
                    public void onDeleteSet(Workout workout, Exercise exercise, ExerciseSet set) {
                        viewModel.deleteSet(workout.getId(), set.getId(), view);
                    }
                }
        );

        binding.rvWorkouts.setAdapter(adapter);

        viewModel.getWorkouts().observe(getViewLifecycleOwner(), workouts -> {
            if (workouts != null) {
                adapter.submitList(List.of());
                adapter.submitList(new ArrayList<>(workouts));
            }
        });

        viewModel.loadWorkouts(view);

        binding.addWorkoutFAB.setOnClickListener(v -> {
            Navigation.findNavController(view)
                    .navigate(R.id.action_workoutsFragment_to_workoutCreationFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
