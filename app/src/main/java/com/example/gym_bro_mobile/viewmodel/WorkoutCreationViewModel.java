package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.repository.WorkoutPlanRepository;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@HiltViewModel
public class WorkoutCreationViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;
    private final WorkoutPlanRepository workoutPlanRepository;

    private final MutableLiveData<List<WorkoutPlan>> workouts = new MutableLiveData<>();

    @Inject
    public WorkoutCreationViewModel(OkHttpClient client, Gson gson, Application app, JwtService jwtService, WorkoutPlanRepository workoutPlanRepository) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    public void startWorkout(Long workoutPlanId, View view) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("workoutPlanId", workoutPlanId);

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getString(R.string.api_url) + "/workout/create")
                        .addHeader("Authorization", "Bearer " + jwtService.getToken())
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        navigateToWorkouts(view);
                    } else if (response.code() == 401) {
                        navigateToAuth(view);
                    } else {
                        Log.e("WorkoutPlanFormVM", "Server error: " + response.code());
                    }
                }
            } catch (IOException | JSONException e) {
                Log.e("WorkoutPlanFormVM", "Network error: ", e);
            }
        }).start();
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workouts;
    }

    public void loadWorkoutPlans(View view) {
        workoutPlanRepository.loadWorkoutPlans(view, workouts);
    }

    private void navigateToWorkouts(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_workoutCreationFragment_to_workoutsFragment));
    }

    private void navigateToAuth(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_workoutCreationFragment_to_authFragment));
    }
}
