package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExercisePage;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.repository.ExerciseRepository;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@HiltViewModel
public class WorkoutPlanFormViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;
    private final ExerciseRepository exerciseRepository;

    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();
    private final MutableLiveData<WorkoutPlan> currentPlan = new MutableLiveData<>();

    @Inject
    public WorkoutPlanFormViewModel(OkHttpClient client, Gson gson, Application app,
                                    JwtService jwtService, ExerciseRepository exerciseRepository) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
        this.exerciseRepository = exerciseRepository;
        currentPlan.setValue(new WorkoutPlan());
    }

    public void initEditPlan(WorkoutPlan plan) {
        currentPlan.setValue(plan);
    }

    public LiveData<WorkoutPlan> getCurrentPlan() {
        return currentPlan;
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public void loadExercises(View view) {
        exerciseRepository.loadExercises(view, exercises);
    }


    public void createWorkoutPlan(WorkoutPlan plan, View view) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("name", plan.getName());

                if (plan.getExercises() != null && !plan.getExercises().isEmpty()) {
                    json.put("exercises", new JSONArray(gson.toJson(plan.getExercises())));
                }
                if (plan.getSetsReps() != null && !plan.getSetsReps().isEmpty()) {
                    json.put("setsReps", new JSONArray(gson.toJson(plan.getSetsReps())));
                }

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getString(R.string.api_url) + "/workout-plan/create")
                        .addHeader("Authorization", "Bearer " + jwtService.getToken())
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        navigateToWorkoutPlans(view);
                    } else {
                        if (response.code() == 401) {
                            navigateToAuth(view);
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                Log.e("ExerciseFormVM", "Error: ", e);
            }
        }).start();
    }

    private void navigateToWorkoutPlans(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_workoutPlanFormFragment_to_workoutPlansFragment));
    }

    private void navigateToAuth(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_workoutPlanFormFragment_to_authFragment));
    }
}
