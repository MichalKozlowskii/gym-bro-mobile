package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.model.WorkoutPlan;
import com.example.gym_bro_mobile.model.WorkoutPlanPage;
import com.example.gym_bro_mobile.service.JwtService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@HiltViewModel
public class WorkoutPlansViewModel extends ViewModel {
    private final OkHttpClient client;
    private final Gson gson;
    private final Application app;
    private final JwtService jwtService;
    private final MutableLiveData<List<WorkoutPlan>> workoutPlans = new MutableLiveData<>();

    @Inject
    public WorkoutPlansViewModel(OkHttpClient client, Gson gson, Application app, JwtService jwtService) {
        this.client = client;
        this.gson = gson;
        this.app = app;
        this.jwtService = jwtService;
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workoutPlans;
    }

    public void loadWorkoutPlans(View view) {
        new Thread(() -> {
            String jwt = jwtService.getToken();

            Request request = new Request.Builder()
                    .url(app.getString(R.string.api_url) + "/workout-plan?pageNumber=1&pageSize=1000")
                    .addHeader("Authorization", "Bearer " + jwt)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    WorkoutPlanPage page = gson.fromJson(response.body().charStream(), WorkoutPlanPage.class);
                    List<WorkoutPlan> list = page.getContent();

                    workoutPlans.postValue(list);
                } else {
                    if (response.code() == 401) {
                        view.post(() -> Navigation.findNavController(view)
                                .navigate(R.id.action_workoutPlansFragment_to_authFragment));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
