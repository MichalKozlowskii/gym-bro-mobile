package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExercisePage;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import okhttp3.Request;
import okhttp3.Response;

@HiltViewModel
public class ExercisesViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;
    private final MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();

    @Inject
    public ExercisesViewModel(OkHttpClient client, Gson gson, Application app, JwtService jwtService) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public void loadExercises(View view) {
        new Thread(() -> {
            String jwt = jwtService.getToken();

            Request request = new Request.Builder()
                    .url(app.getString(R.string.api_url) + "/exercise?pageNumber=1&pageSize=1000")
                    .addHeader("Authorization", "Bearer " + jwt)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    ExercisePage page = gson.fromJson(response.body().charStream(), ExercisePage.class);
                    List<Exercise> list = page.getContent();

                    exercises.postValue(list);
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