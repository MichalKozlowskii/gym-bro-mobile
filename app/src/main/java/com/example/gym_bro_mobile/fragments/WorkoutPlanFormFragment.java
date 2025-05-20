package com.example.gym_bro_mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gym_bro_mobile.databinding.FragmentWorkoutPlanFormBinding;
import com.example.gym_bro_mobile.databinding.ItemExerciseEntryBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.SetsReps;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.viewmodel.WorkoutPlanFormViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkoutPlanFormFragment extends Fragment {
    private FragmentWorkoutPlanFormBinding binding;
    private WorkoutPlanFormViewModel viewModel;
    private final List<ItemExerciseEntryBinding> exerciseBindings = new ArrayList<>();
    private final Gson gson = new Gson();

    public static final String ARG_WORKOUT_PLAN_JSON = "workout_plan_json";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutPlanFormBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WorkoutPlanFormViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        boolean isEditing = false;

        if (args != null && args.containsKey(ARG_WORKOUT_PLAN_JSON)) {
            String json = args.getString(ARG_WORKOUT_PLAN_JSON);
            WorkoutPlan plan = gson.fromJson(json, WorkoutPlan.class);
            viewModel.initEditPlan(plan);
            isEditing = true;
        }

        if (isEditing) {
            binding.btnDelete.setVisibility(View.VISIBLE);
        } else {
            binding.btnDelete.setVisibility(View.GONE);
        }

        viewModel.currentPlan.observe(getViewLifecycleOwner(), plan -> {
            if (plan == null) return;
            binding.etPlanName.setText(plan.getName());

            if (plan.getExercises() != null) {
                for (int i = 0; i < plan.getExercises().size(); i++) {
                    String name = plan.getExercises().get(i).getName();
                    int sets = plan.getSetsReps().get(i).getSets();
                    int reps = plan.getSetsReps().get(i).getReps();
                    addExerciseRow(name, sets, reps);
                }
            }
        });

        binding.btnAddExercise.setOnClickListener(v -> addExerciseRow());

        binding.btnSave.setOnClickListener(v -> {
            WorkoutPlan plan = viewModel.currentPlan.getValue();
            if (plan == null) plan = new WorkoutPlan();

            plan.setName(binding.etPlanName.getText().toString().trim());

            List<Exercise> exercises = new ArrayList<>();
            List<SetsReps> setsRepsList = new ArrayList<>();

            for (ItemExerciseEntryBinding exBinding : exerciseBindings) {
                Exercise ex = new Exercise();
                ex.setName(exBinding.etExerciseName.getText().toString().trim());

                SetsReps sr = new SetsReps();
                sr.setSets(Integer.parseInt(exBinding.etSets.getText().toString()));
                sr.setReps(Integer.parseInt(exBinding.etReps.getText().toString()));

                exercises.add(ex);
                setsRepsList.add(sr);
            }

            plan.setExercises(exercises);
            plan.setSetsReps(setsRepsList);

            if (plan.getId() == null) {
                //viewModel.createWorkoutPlan(plan);
            } else {
                //viewModel.updateWorkoutPlan(plan);
            }

            // pop back or navigate away
        });

        binding.btnDelete.setOnClickListener(v -> {
            WorkoutPlan plan = viewModel.currentPlan.getValue();
            if (plan != null && plan.getId() != null) {
                //viewModel.deleteWorkoutPlan(plan.getId());
                // pop back or navigate away
            }
        });
    }

    private void addExerciseRow() {
        addExerciseRow("", 0, 0);
    }

    private void addExerciseRow(String name, int sets, int reps) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ItemExerciseEntryBinding exBinding = ItemExerciseEntryBinding.inflate(inflater, binding.exerciseContainer, false);

        exBinding.etExerciseName.setText(name);
        exBinding.etSets.setText(sets > 0 ? String.valueOf(sets) : "");
        exBinding.etReps.setText(reps > 0 ? String.valueOf(reps) : "");

        binding.exerciseContainer.addView(exBinding.getRoot());
        exerciseBindings.add(exBinding);
    }
}


