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
import com.example.gym_bro_mobile.databinding.FragmentWorkoutsBinding;
import com.example.gym_bro_mobile.model.Workout;
import com.example.gym_bro_mobile.rv.workout.OnWorkoutClickListener;
import com.example.gym_bro_mobile.rv.workout.WorkoutAdapter;
import com.example.gym_bro_mobile.viewmodel.WorkoutsViewModel;
import com.google.gson.Gson;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutsFragment extends Fragment {
    private FragmentWorkoutsBinding binding;
    private WorkoutsViewModel viewModel;
    private WorkoutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().findViewById(com.example.gym_bro_mobile.R.id.bottom_navigation).setVisibility(View.VISIBLE);

        viewModel.getWorkouts().observe(getViewLifecycleOwner(), this::updateWorkoutList);
        viewModel.loadWorkouts(view);

        setupRecyclerView();

        binding.addWorkoutFAB.setOnClickListener(v -> {
            Navigation.findNavController(view)
                    .navigate(R.id.action_workoutsFragment_to_workoutCreationFragment);
        });
    }

    private void setupRecyclerView() {
        adapter = new WorkoutAdapter(new OnWorkoutClickListener() {
            @Override
            public void onClick(Workout workout) {
                Bundle args = new Bundle();
                String workoutJson = new Gson().toJson(workout);
                args.putString(WorkoutPlanFormFragment.ARG_WORKOUT_PLAN_JSON, workoutJson);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_workoutPlansFragment_to_workoutPlanFormFragment, args); // TODO: change
            }
        });

        binding.rvWorkouts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvWorkouts.setAdapter(adapter);
    }

    private void updateWorkoutList(List<Workout> workouts) {
        adapter.submitList(workouts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
