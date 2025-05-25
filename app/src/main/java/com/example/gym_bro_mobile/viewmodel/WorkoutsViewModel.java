package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.gson.LocalDateTimeAdapter;
import com.example.gym_bro_mobile.gson.WorkoutDeserializer;
import com.example.gym_bro_mobile.model.Workout;
import com.example.gym_bro_mobile.model.WorkoutPage;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@HiltViewModel
public class WorkoutsViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;
    private final MutableLiveData<List<Workout>> workouts = new MutableLiveData<>();

    @Inject
    public WorkoutsViewModel(OkHttpClient client, Application app, JwtService jwtService) {
        this.client = client;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Workout.class, new WorkoutDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        this.app = app;
        this.jwtService = jwtService;
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workouts;
    }

    public void loadWorkouts(View view) {
        new Thread(() -> {
            String jwt = jwtService.getToken();

            Request request = new Request.Builder()
                    .url(app.getString(R.string.api_url) + "/workout?pageNumber=1&pageSize=1000")
                    .addHeader("Authorization", "Bearer " + jwt)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkoutPage page = gson.fromJson(response.body().charStream(), WorkoutPage.class);
                    List<Workout> list = page.getContent();

                    workouts.postValue(list);
                } else {
                    if (response.code() == 401) {
                        view.post(() -> Navigation.findNavController(view)
                                .navigate(R.id.action_workoutCreationFragment_to_authFragment));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addSet(Long workoutId, Long exerciseId, int reps, float weight, View view) {
        new Thread(() -> {
            try {
                JsonObject exerciseObj = new JsonObject();
                exerciseObj.addProperty("id", exerciseId);

                JsonObject payload = new JsonObject();
                payload.add("exercise", exerciseObj);
                payload.addProperty("weight", weight);
                payload.addProperty("reps", reps);

                RequestBody body = RequestBody.create(
                        payload.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getString(R.string.api_url) + "/workout/" + workoutId + "/addset")
                        .addHeader("Authorization", "Bearer " + jwtService.getToken())
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        loadWorkouts(view);
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

    public void deleteWorkout(Long workoutId, View view) {
        new Thread(() -> {
            String jwt = jwtService.getToken();

            Request request = new Request.Builder()
                    .url(app.getString(R.string.api_url) + "/workout/" + workoutId)
                    .addHeader("Authorization", "Bearer " + jwt)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    loadWorkouts(view);
                } else {
                    if (response.code() == 401) {
                        navigateToAuth(view);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void deleteSet(Long workoutId, Long setId, View view) {
        new Thread(() -> {
            String jwt = jwtService.getToken();

            Request request = new Request.Builder()
                    .url(app.getString(R.string.api_url) + "/workout/" + workoutId + "/deleteset/" + setId)
                    .addHeader("Authorization", "Bearer " + jwt)
                    .delete()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    loadWorkouts(view);
                } else {
                    if (response.code() == 401) {
                        navigateToAuth(view);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void navigateToAuth(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_workoutsFragment_to_authFragment));
    }
}