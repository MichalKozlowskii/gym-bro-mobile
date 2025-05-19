package com.example.gym_bro_mobile.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.gym_bro_mobile.R;
import com.example.gym_bro_mobile.service.JwtService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<String> resultMessage = new MutableLiveData<>();
    private final OkHttpClient client = new OkHttpClient();

    public LiveData<String> getResultMessage() {
        return resultMessage;
    }

    public void validateJWT(Context context, View view) {
        String jwt = new JwtService(context).getToken();

        Request request = new Request.Builder()
                .url(context.getString(R.string.api_url) + "/exercise")
                .addHeader("Authorization", "Bearer " + jwt)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    view.post(() -> {
                        Navigation.findNavController(view).navigate(R.id.action_authFragment_to_mainFragment);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                resultMessage.postValue("Network error: " + e.getMessage());
                Log.d("AuthViewModel", e.getMessage());
            }
        });
    }

    public void login(String username, String password, Context context) {
        makeAuthRequest(username, password, context, "/login", true);
    }

    public void register(String username, String password, Context context) {
        makeAuthRequest(username, password, context, "/register", false);
    }

    private void makeAuthRequest(String username, String password, Context context, String endpoint, boolean isLogin) {
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
                .url(context.getString(R.string.api_url) + endpoint)
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
                            new JwtService(context).saveToken(token);
                            resultMessage.postValue("Login successful\nJWT: " + token);
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
                Log.d("AuthViewModel", e.getMessage());
            }
        });
    }
}
