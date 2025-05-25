package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.Stats;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@HiltViewModel
public class ExerciseStatsViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;

    private final MutableLiveData<Stats> stats = new MutableLiveData<>();

    @Inject
    public ExerciseStatsViewModel(OkHttpClient client, Gson gson, Application app, JwtService jwtService) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
    }

    public LiveData<Stats> getStats() {
        return stats;
    }

    public void loadStats(long exerciseId, View view) {
        new Thread(() -> {
            String jwt = jwtService.getToken();

            Request request = new Request.Builder()
                    .url(app.getString(R.string.api_url) + "/stats/" + exerciseId)
                    .addHeader("Authorization", "Bearer " + jwt)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    Stats exerciseStats = gson.fromJson(response.body().charStream(), Stats.class);

                    stats.postValue(exerciseStats);
                } else {
                    if (response.code() == 401) {
                        view.post(() -> Navigation.findNavController(view)
                                .navigate(R.id.action_exerciseStatsFragment_to_authFragment));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
