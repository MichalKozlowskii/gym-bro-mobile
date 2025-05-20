package com.example.gym_bro_mobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gym_bro_mobile.databinding.FragmentWorkoutPlansBinding;
import com.example.gym_bro_mobile.viewmodel.WorkoutPlansViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutPlansFragment extends Fragment {
    private FragmentWorkoutPlansBinding binding;
    private WorkoutPlansViewModel workoutPlansViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutPlansBinding.inflate(inflater, container, false);
        workoutPlansViewModel = new ViewModelProvider(this).get(WorkoutPlansViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().findViewById(com.example.gym_bro_mobile.R.id.bottom_navigation).setVisibility(View.VISIBLE);

        workoutPlansViewModel.getWorkoutPlans().observe(getViewLifecycleOwner(), workoutPlans -> {
            Log.d("11", "elo");
            if (workoutPlans != null) {
                for (int i = 0; i < workoutPlans.size(); i++) {
                    // Log each workout plan's name (or any property you want to check)
                    android.util.Log.d("WorkoutPlansFragment", "Plan #" + i + ": " + workoutPlans.get(i).getName());
                }
            } else {
                android.util.Log.d("WorkoutPlansFragment", "No workout plans received");
            }
        });
        workoutPlansViewModel.loadWorkoutPlans(view);
    }

}
