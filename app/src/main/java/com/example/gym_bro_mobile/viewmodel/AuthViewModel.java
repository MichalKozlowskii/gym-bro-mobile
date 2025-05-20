package com.example.gym_bro_mobile.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.service.JwtService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final OkHttpClient client;
    private final JwtService jwtService;
    private final Application app;
    private final MutableLiveData<String> resultMessage = new MutableLiveData<>();

    @Inject
    public AuthViewModel(OkHttpClient client, JwtService jwtService, Application app) {
        this.client = client;
        this.jwtService = jwtService;
        this.app = app;
    }

    public LiveData<String> getResultMessage() {
        return resultMessage;
    }

    public void validateJWT(View view) {
        String jwt = jwtService.getToken();

        Request request = new Request.Builder()
                .url(app.getString(R.string.api_url) + "/exercise")
                .addHeader("Authorization", "Bearer " + jwt)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    navigateToApp(view);
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                view.post(() -> resultMessage.setValue("Network error: " + e.getMessage()));
                Log.e("AuthViewModel", e.getMessage(), e);
            }
        });
    }

    public void login(String username, String password, View view) {
        makeAuthRequest(username, password, "/login", true, view);
    }

    public void register(String username, String password, View view) {
        makeAuthRequest(username, password, "/register", false, view);
    }

    private void navigateToApp(View view) {
        view.post(() -> Navigation.findNavController(view)
                .navigate(R.id.action_authFragment_to_exercisesFragment));
    }

    private void makeAuthRequest(String username, String password, String endpoint, boolean isLogin, View view) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            resultMessage.setValue("JSON error");
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(), MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(app.getString(R.string.api_url) + endpoint)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    if (isLogin) {
                        try {
                            JSONObject resJson = new JSONObject(responseBody);
                            String token = resJson.getString("jwt_token");
                            jwtService.saveToken(token);
                            navigateToApp(view);
                        } catch (JSONException e) {
                            resultMessage.postValue("Invalid response format");
                        }
                    } else {
                        resultMessage.postValue("Registration successful");
                    }
                } else {
                    resultMessage.postValue((isLogin ? "Login" : "Registration") + " failed: " + responseBody);
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                resultMessage.postValue("Network error: " + e.getMessage());
                Log.e("AuthViewModel", e.getMessage(), e);
            }
        });
    }
}

