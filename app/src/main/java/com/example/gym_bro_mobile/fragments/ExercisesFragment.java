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

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.databinding.FragmentExercisesBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.viewmodel.ExercisesViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExercisesFragment extends Fragment {
    private FragmentExercisesBinding binding;
    private ExercisesViewModel exercisesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentExercisesBinding.inflate(inflater, container, false);
        exercisesViewModel = new ViewModelProvider(this).get(ExercisesViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);

        exercisesViewModel.loadExercises(view);

        exercisesViewModel.getExercises().observe(getViewLifecycleOwner(), exerciseList -> {
            for (Exercise exercise : exerciseList) {
                Log.d("ExercisesFragment", "Exercise: " + exercise.getName());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
