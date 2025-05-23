package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.gson.LocalDateTimeAdapter;
import com.example.gym_bro_mobile.gson.WorkoutDeserializer;
import com.example.gym_bro_mobile.model.Workout;
import com.example.gym_bro_mobile.model.WorkoutPage;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

                    Log.d("11", list.get(0).toString());

                    workouts.postValue(list);
                } else {
                    if (response.code() == 401) {
                        view.post(() -> Navigation.findNavController(view)
                                .navigate(R.id.action_workoutPlansFragment_to_authFragment)); // change
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
