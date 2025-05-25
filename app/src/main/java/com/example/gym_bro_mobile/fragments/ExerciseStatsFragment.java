package com.example.gym_bro_mobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gym_bro_mobile.databinding.FragmentExerciseStatsBinding;
import com.example.gym_bro_mobile.model.ExerciseSet;
import com.example.gym_bro_mobile.model.Stats;
import com.example.gym_bro_mobile.viewmodel.ExerciseStatsViewModel;
import com.example.gym_bro_mobile.viewmodel.ExercisesViewModel;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ExerciseStatsFragment extends Fragment {
    private FragmentExerciseStatsBinding binding;
    private ExerciseStatsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentExerciseStatsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ExerciseStatsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().findViewById(com.example.gym_bro_mobile.R.id.bottom_navigation).setVisibility(View.VISIBLE);

        if (getArguments() != null && getArguments().containsKey("exerciseId")) {
            long exerciseId = getArguments().getLong("exerciseId");

            viewModel.getStats().observe(getViewLifecycleOwner(), this::setupUI);
            viewModel.loadStats(exerciseId, view);
        }
    }

    private void setupUI(Stats stats) {
        if (stats == null) return;
        binding.tvExerciseName.setText(stats.getMostWeightSet().getExercise().getName());

        setupSets(stats);
        setupPlot(stats);
    }

    private void setupSets(Stats stats) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        setupSet(stats.getMostWeightSet(), "Most weight: ", binding.tvMostWeight, formatter);
        setupSet(stats.getLeastWeightSet(), "Least weight: ", binding.tvLeastWeight, formatter);
        setupSet(stats.getMostRepsSet(), "Most reps: ", binding.tvMostReps, formatter);
        setupSet(stats.getLeastRepsSet(), "Least reps: ", binding.tvLeastReps, formatter);
        setupSet(stats.getNewestSet(), "Newest set: ", binding.tvLastSet, formatter);
        setupSet(stats.getOldestSet(), "Oldest set: ", binding.tvMostRecent, formatter);
    }

    private void setupSet(ExerciseSet set, String prefix, TextView textView, DateTimeFormatter formatter) {
        textView.setText(prefix + set.getReps() + " x " + Math.round(set.getWeight() * 100) / 100. + " kg (" + set.getCreationDate().format(formatter) + ")");
    }

    private void setupPlot(Stats stats) {
        if (stats.getPlotData().isEmpty()) return;

        GraphView graph = binding.graph;

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        List<ExerciseSet> sets = stats.getPlotData();

        for (ExerciseSet set : sets) {
            LocalDateTime dateTime = set.getCreationDate();
            double x = toMillis(dateTime);
            double y = set.getWeight();

            series.appendData(new DataPoint(x, y), true, sets.size());
        }

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(graph.getContext())
        );
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(30);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("date");
        graph.getGridLabelRenderer().setVerticalAxisTitle("most weight (kg)");

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(toMillis(sets.get(0).getCreationDate()));
        graph.getViewport().setMaxX(toMillis(sets.get(sets.size() - 1).getCreationDate()));

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(6);
    }


    private long toMillis(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
