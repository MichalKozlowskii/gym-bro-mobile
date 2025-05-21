package com.example.gym_bro_mobile.repository;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.Exercise;
import com.example.gym_bro_mobile.model.ExercisePage;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class ExerciseRepository {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;

    @Inject
    public ExerciseRepository(OkHttpClient client, Gson gson, Application app, JwtService jwtService) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
    }

    public void loadExercises(View view, MutableLiveData<List<Exercise>> exercisesLiveData) {
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
                    exercisesLiveData.postValue(page.getContent());
                } else if (response.code() == 401) {
                    view.post(() -> Navigation.findNavController(view)
                            .navigate(R.id.action_exercisesFragment_to_authFragment));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

