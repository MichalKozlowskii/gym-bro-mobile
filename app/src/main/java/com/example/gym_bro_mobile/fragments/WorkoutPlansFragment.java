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
import com.example.gym_bro_mobile.databinding.FragmentWorkoutPlansBinding;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.rv.workoutplan.OnWorkoutPlanClickListener;
import com.example.gym_bro_mobile.rv.workoutplan.WorkoutPlanAdapter;
import com.example.gym_bro_mobile.viewmodel.WorkoutPlansViewModel;
import com.google.gson.Gson;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutPlansFragment extends Fragment {
    private FragmentWorkoutPlansBinding binding;
    private WorkoutPlansViewModel workoutPlansViewModel;
    private WorkoutPlanAdapter adapter;

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

        setupRecyclerView();

        workoutPlansViewModel.getWorkoutPlans().observe(getViewLifecycleOwner(), this::updateWorkoutPlanList);
        workoutPlansViewModel.loadWorkoutPlans(view);

        binding.addWorkoutPlanFAB.setOnClickListener(v -> {
            Navigation.findNavController(view)
                    .navigate(R.id.action_workoutPlansFragment_to_workoutPlanFormFragment);
        });
    }

    private void setupRecyclerView() {
        adapter = new WorkoutPlanAdapter(new OnWorkoutPlanClickListener() {
            @Override
            public void onClick(WorkoutPlan plan) {
                Bundle args = new Bundle();
                String planJson = new Gson().toJson(plan);
                args.putString(WorkoutPlanFormFragment.ARG_WORKOUT_PLAN_JSON, planJson);
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_workoutPlansFragment_to_workoutPlanFormFragment, args);
            }
        });

        binding.rvWorkoutplans.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvWorkoutplans.setAdapter(adapter);
    }

    private void updateWorkoutPlanList(List<WorkoutPlan> workoutPlans) {
        adapter.submitList(workoutPlans);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
