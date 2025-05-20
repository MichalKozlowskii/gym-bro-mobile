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

import com.example.gym_bro_mobile.databinding.FramgentExerciseFormBinding;
import com.example.gym_bro_mobile.viewmodel.ExerciseFormViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExerciseFormFragment extends Fragment {
    private FramgentExerciseFormBinding binding;
    private ExerciseFormViewModel exerciseFormViewModel;
    private Long id = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FramgentExerciseFormBinding.inflate(inflater, container, false);
        exerciseFormViewModel = new ViewModelProvider(this).get(ExerciseFormViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().findViewById(com.example.gym_bro_mobile.R.id.bottom_navigation).setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            id = getArguments().getLong("exerciseId");
            final String demonstrationUrl = getArguments().getString("exerciseDemonstrationUrl");
            final String name = getArguments().getString("exerciseName");

            binding.deleteButton.setVisibility(View.VISIBLE);
            binding.nameInput.setText(name);
            binding.urlInput.setText(demonstrationUrl);

            binding.deleteButton.setOnClickListener(v -> {
                exerciseFormViewModel.deleteExercise(id, v);
            });
        }

        binding.saveButton.setOnClickListener(v -> {
            String name = binding.nameInput.getText().toString();
            String demonstrationUrl = binding.urlInput.getText().toString();

            if (name.isBlank()) return;

            if (id == null) {
                exerciseFormViewModel.saveExercise(name, demonstrationUrl, view);
            } else {
                exerciseFormViewModel.updateExercise(id, name, demonstrationUrl, view);
            }
        });
    }
}
