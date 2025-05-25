package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.model.WorkoutPlanPage;
import com.example.gym_bro_mobile.repository.WorkoutPlanRepository;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@HiltViewModel
public class WorkoutPlansViewModel extends ViewModel {
    private final WorkoutPlanRepository workoutPlanRepository;
    private final MutableLiveData<List<WorkoutPlan>> workoutPlans = new MutableLiveData<>();

    @Inject
    public WorkoutPlansViewModel(WorkoutPlanRepository workoutPlanRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workoutPlans;
    }

    public void loadWorkoutPlans(View view) {
        workoutPlanRepository.loadWorkoutPlans(view, workoutPlans);
    }
}
