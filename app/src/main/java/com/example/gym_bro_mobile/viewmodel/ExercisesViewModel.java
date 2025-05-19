package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.Exercise;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@HiltViewModel
public class ExercisesViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();

    @Inject
    public ExercisesViewModel(OkHttpClient client, Gson gson, Application app) {
        this.client = client;
        this.gson = gson;
        this.app = app;
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public void loadExercises(View view) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url("https://your-api.com/api/exercises")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    Type listType = new TypeToken<List<Exercise>>() {}.getType();
                    List<Exercise> parsed = gson.fromJson(response.body().charStream(), listType);

                    // Post to LiveData from background thread
                    exercises.postValue(parsed);
                } else {
                    if (response.code() == 401) {
                        view.post(() -> Navigation.findNavController(view)
                                .navigate(R.id.action_exercisesFragment_to_authFragment));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // or log it
            }
        }).start();
    }
}