package com.example.gym_bro_mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gym_bro_mobile.databinding.FragmentWorkoutCreationBinding;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.viewmodel.WorkoutCreationViewModel;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutCreationFragment extends Fragment {
    private FragmentWorkoutCreationBinding binding;
    private WorkoutCreationViewModel viewModel;
    private List<WorkoutPlan> workoutPlanList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutCreationBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WorkoutCreationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.loadWorkoutPlans(view);

        viewModel.getWorkoutPlans().observe(getViewLifecycleOwner(), plans -> {
            if (plans != null) {
                workoutPlanList = plans;
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        workoutPlanList.stream().map(WorkoutPlan::getName).collect(Collectors.toList())
                );
                binding.workoutPlanSpinner.setAdapter(adapter);
            }
        });

        binding.createWorkoutButton.setOnClickListener(v -> {
            int position = binding.workoutPlanSpinner.getSelectedItemPosition();
            if (position != -1 && workoutPlanList != null) {
                WorkoutPlan selectedPlan = workoutPlanList.get(position);
                viewModel.startWorkout(selectedPlan.getId(), v);
            }
        });
    }
}
