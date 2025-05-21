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
        try {
            JSONObject json = buildWorkoutPlanJson(plan);
            String url = app.getString(R.string.api_url) + "/workout-plan/create";
            sendWorkoutPlanRequest(url, "POST", json, view);
        } catch (JSONException e) {
            Log.e("WorkoutPlanFormVM", "JSON error: ", e);
        }
    }

    public void updateWorkoutPlan(WorkoutPlan plan, View view) {
        try {
            JSONObject json = buildWorkoutPlanJson(plan);
            String url = app.getString(R.string.api_url) + "/workout-plan/" + plan.getId();
            sendWorkoutPlanRequest(url, "PUT", json, view);
        } catch (JSONException e) {
            Log.e("WorkoutPlanFormVM", "JSON error: ", e);
        }
    }


    private void sendWorkoutPlanRequest(String url, String method, JSONObject json, View view) {
        new Thread(() -> {
            try {
                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request.Builder builder = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "Bearer " + jwtService.getToken());

                if ("POST".equalsIgnoreCase(method)) {
                    builder.post(body);
                } else if ("PUT".equalsIgnoreCase(method)) {
                    builder.put(body);
                } else {
                    throw new IllegalArgumentException("Unsupported HTTP method: " + method);
                }

                Request request = builder.build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        navigateToWorkoutPlans(view);
                    } else if (response.code() == 401) {
                        navigateToAuth(view);
                    } else {
                        Log.e("WorkoutPlanFormVM", "Server error: " + response.code());
                    }
                }
            } catch (IOException e) {
                Log.e("WorkoutPlanFormVM", "Network error: ", e);
            }
        }).start();
    }


    private JSONObject buildWorkoutPlanJson(WorkoutPlan plan) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", plan.getName());

        if (plan.getExercises() != null && !plan.getExercises().isEmpty()) {
            JSONArray exercisesArray = new JSONArray();
            for (Exercise e : plan.getExercises()) {
                JSONObject obj = new JSONObject();
                obj.put("id", e.getId());
                exercisesArray.put(obj);
            }
            json.put("exercises", exercisesArray);
        }

        if (plan.getSetsReps() != null && !plan.getSetsReps().isEmpty()) {
            json.put("setsReps", new JSONArray(gson.toJson(plan.getSetsReps())));
        }

        return json;
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
