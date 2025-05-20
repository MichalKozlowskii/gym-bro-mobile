package com.example.gym_bro_mobile.fragments;

import android.content.Intent;
import android.net.Uri;
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
import com.example.gym_bro_mobile.databinding.FragmentExercisesBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.rv.ExerciseAdapter;
import com.example.gym_bro_mobile.rv.OnExerciseClickListener;
import com.example.gym_bro_mobile.viewmodel.ExercisesViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExercisesFragment extends Fragment {

    private FragmentExercisesBinding binding;
    private ExercisesViewModel exercisesViewModel;
    private ExerciseAdapter adapter;

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

        requireActivity().findViewById(com.example.gym_bro_mobile.R.id.bottom_navigation).setVisibility(View.VISIBLE);

        setupRecyclerView();

        exercisesViewModel.getExercises().observe(getViewLifecycleOwner(), this::updateExercises);
        exercisesViewModel.loadExercises(view);

        binding.addExerciseFab.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_exercisesFragment_to_exerciseFormFragment);
        });
    }

    private void setupRecyclerView() {
        adapter = new ExerciseAdapter(new OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise) {
                Bundle args = new Bundle();
                args.putLong("exerciseId", exercise.getId());
                args.putString("exerciseName", exercise.getName());
                args.putString("exerciseDemonstrationUrl", exercise.getDemonstrationUrl());

                Navigation.findNavController(requireView())
                        .navigate(R.id.action_exercisesFragment_to_exerciseFormFragment, args);
            }

            @Override
            public void onThumbnailClick(String videoUrl) {
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    intent.setPackage("com.google.android.youtube"); // optional: force YouTube app
                    startActivity(intent);
                }
            }
        });

        binding.rvExercises.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvExercises.setAdapter(adapter);
    }

    private void updateExercises(List<Exercise> exercises) {
        adapter.submitList(exercises);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
