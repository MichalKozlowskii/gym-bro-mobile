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

import com.example.gym_bro_mobile.databinding.FragmentWorkoutPlanFormBinding;
import com.example.gym_bro_mobile.databinding.ItemExerciseEntryBinding;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.SetsReps;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.viewmodel.WorkoutPlanFormViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutPlanFormBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WorkoutPlanFormViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        boolean isEditing = false;
        Bundle args = getArguments();

        if (args != null && args.containsKey(ARG_WORKOUT_PLAN_JSON)) {
            String json = args.getString(ARG_WORKOUT_PLAN_JSON);
            WorkoutPlan plan = gson.fromJson(json, WorkoutPlan.class);
            viewModel.initEditPlan(plan);
            isEditing = true;
        }

        binding.btnDelete.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            WorkoutPlan plan = viewModel.getCurrentPlan().getValue();
            if (plan != null && plan.getExercises() != null) {
                binding.exerciseContainer.removeAllViews();
                exerciseBindings.clear();
                for (int i = 0; i < plan.getExercises().size(); i++) {
                    addExerciseRow(plan.getExercises().get(i), plan.getSetsReps().get(i));
                }
            }
        });

        viewModel.loadExercises(view);

        viewModel.getCurrentPlan().observe(getViewLifecycleOwner(), plan -> {
            if (plan == null) return;
            binding.etPlanName.setText(plan.getName());
        });

        binding.btnAddExercise.setOnClickListener(v -> addExerciseRow());

        binding.btnSave.setOnClickListener(v -> {
            WorkoutPlan plan = viewModel.getCurrentPlan().getValue();
            if (plan == null) plan = new WorkoutPlan();

            plan.setName(binding.etPlanName.getText().toString().trim());

            List<Exercise> selectedExercises = new ArrayList<>();
            List<SetsReps> setsRepsList = new ArrayList<>();

            for (ItemExerciseEntryBinding exBinding : exerciseBindings) {
                int selectedPos = exBinding.spinnerExercise.getSelectedItemPosition();
                Exercise selectedExercise = viewModel.getExercises().getValue().get(selectedPos);

                int sets = Integer.parseInt(exBinding.etSets.getText().toString());
                int reps = Integer.parseInt(exBinding.etReps.getText().toString());

                selectedExercises.add(selectedExercise);
                setsRepsList.add(new SetsReps(sets, reps));
            }

            plan.setExercises(selectedExercises);
            plan.setSetsReps(setsRepsList);

            if (plan.getId() == null) {
                viewModel.createWorkoutPlan(plan, view);
            } else {
                viewModel.updateWorkoutPlan(plan, view);
            }

            // TODO: popBackStack or navigate away
        });

        binding.btnDelete.setOnClickListener(v -> {
            WorkoutPlan plan = viewModel.getCurrentPlan().getValue();
            if (plan != null && plan.getId() != null) {
                // viewModel.deleteWorkoutPlan(plan.getId());
                // TODO: popBackStack or navigate away
            }
        });
    }

    private void addExerciseRow() {
        addExerciseRow(null, new SetsReps(0, 0));
    }

    private void addExerciseRow(Exercise selectedExercise, SetsReps setsReps) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ItemExerciseEntryBinding exBinding = ItemExerciseEntryBinding.inflate(inflater, binding.exerciseContainer, false);

        List<Exercise> exercises = viewModel.getExercises().getValue();
        if (exercises == null) return;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                exercises.stream().map(Exercise::getName).collect(Collectors.toList())
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exBinding.spinnerExercise.setAdapter(adapter);

        if (selectedExercise != null) {
            for (int i = 0; i < exercises.size(); i++) {
                if (exercises.get(i).getId().equals(selectedExercise.getId())) {
                    exBinding.spinnerExercise.setSelection(i);
                    break;
                }
            }
        }

        exBinding.etSets.setText(setsReps.getSets() > 0 ? String.valueOf(setsReps.getSets()) : "");
        exBinding.etReps.setText(setsReps.getReps() > 0 ? String.valueOf(setsReps.getReps()) : "");

        // 🔥 Remove logic
        exBinding.btnRemove.setOnClickListener(v -> {
            binding.exerciseContainer.removeView(exBinding.getRoot());
            exerciseBindings.remove(exBinding);
        });

        binding.exerciseContainer.addView(exBinding.getRoot());
        exerciseBindings.add(exBinding);
    }
}
