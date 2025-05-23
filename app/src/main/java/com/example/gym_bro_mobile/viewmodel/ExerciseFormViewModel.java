package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.service.JwtService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@HiltViewModel
public class ExerciseFormViewModel extends ViewModel {
    private final OkHttpClient client;
    private final JwtService jwtService;
    private final Application app;

    @Inject
    public ExerciseFormViewModel(OkHttpClient client, JwtService jwtService, Application app) {
        this.client = client;
        this.jwtService = jwtService;
        this.app = app;
    }

    public void deleteExercise(Long id, View view) {
        new Thread(() -> {
            try {

                Request request =  new Request.Builder()
                        .url(app.getString(R.string.api_url) + "/exercise/" + id)
                        .addHeader("Authorization", "Bearer " + jwtService.getToken())
                        .delete()
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        navigateToExercises(view);
                    } else {
                        if (response.code() == 401) {
                            navigateToAuth(view);
                        } else if (response.code() == 500) {
                            view.post(() ->
                                    Toast.makeText(
                                            app.getApplicationContext(),
                                            "Can't delete this exercise because it's used in workout plans.",
                                            Toast.LENGTH_LONG).show());
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("ExerciseFormVM", "Error: ", e);
            }
        }).start();
    }

    public void updateExercise(Long id, String name, String demonstrationUrl, View view) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("name", name);
                if (demonstrationUrl != null && !demonstrationUrl.isEmpty()) {
                    json.put("demonstrationUrl", demonstrationUrl);
                }

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getString(R.string.api_url) + "/exercise/" + id)
                        .addHeader("Authorization", "Bearer " + jwtService.getToken())
                        .put(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        navigateToExercises(view);
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

    public void saveExercise(String name, String demonstrationUrl, View view) {
        new Thread(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("name", name);
                if (demonstrationUrl != null && !demonstrationUrl.isEmpty()) {
                    json.put("demonstrationUrl", demonstrationUrl);
                }

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.get("application/json; charset=utf-8")
                );

                Request request = new Request.Builder()
                        .url(app.getString(R.string.api_url) + "/exercise/create")
                        .addHeader("Authorization", "Bearer " + jwtService.getToken())
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        navigateToExercises(view);
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

    private void navigateToExercises(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_exerciseFormFragment_to_exercisesFragment));
    }

    private void navigateToAuth(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_exerciseFormFragment_to_authFragment));
    }
}
