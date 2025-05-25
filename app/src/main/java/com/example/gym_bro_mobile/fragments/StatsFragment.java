package com.example.gym_bro_mobile.fragments;

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
import com.example.gym_bro_mobile.databinding.FragmentStatsBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.rv.exercise.ExerciseAdapter;
import com.example.gym_bro_mobile.rv.exercise.OnExerciseClickListener;
import com.example.gym_bro_mobile.viewmodel.ExercisesViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StatsFragment extends Fragment {
    private FragmentStatsBinding binding;
    private ExercisesViewModel exercisesViewModel;
    private ExerciseAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);
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
    }

    private void setupRecyclerView() {
        adapter = new ExerciseAdapter(new OnExerciseClickListener() {
            @Override
            public void onExerciseClick(Exercise exercise) {
                Bundle args = new Bundle();
                args.putLong("exerciseId", exercise.getId());

                Navigation.findNavController(requireView())
                        .navigate(R.id.action_statsFragment_to_exerciseStatsFragment, args);
            }

            @Override
            public void onThumbnailClick(String videoUrl) {
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
