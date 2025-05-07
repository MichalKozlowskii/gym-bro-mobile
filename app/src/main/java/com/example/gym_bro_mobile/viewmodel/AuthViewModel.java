package com.example.gym_bro_mobile.viewmodel;

import android.content.Context;
import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public LiveData<String> getError() {
        return error;
    }

    public void login(String username, String password, Context context) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
        } catch (JSONException e) {
            error.setValue("JSON error");
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(), MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(context.getString(R.string.api_url) + "/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        String token = resJson.getString("jwt_token");
                        new JwtService(context).saveToken(token);
                    } catch (JSONException e) {
                        error.postValue("Invalid response format");
                    }
                } else {
                    error.postValue("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }
        });
    }

    public void register(String username, String password, Context context) {
        // Implement similarly to login()
    }
}
