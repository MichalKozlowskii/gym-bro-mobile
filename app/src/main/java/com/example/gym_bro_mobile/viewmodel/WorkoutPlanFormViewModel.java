package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;

@HiltViewModel
public class WorkoutPlanFormViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;

    public final MutableLiveData<WorkoutPlan> currentPlan = new MutableLiveData<>();

    @Inject
    public WorkoutPlanFormViewModel(OkHttpClient client, Gson gson, Application app, JwtService jwtService) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
        currentPlan.setValue(new WorkoutPlan());
    }

    public void initEditPlan(WorkoutPlan plan) {
        currentPlan.setValue(plan);
    }

    // save/delete logic with client can be added here (POST/PUT/DELETE calls)
}
