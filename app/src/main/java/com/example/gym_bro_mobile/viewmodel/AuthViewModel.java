package com.example.gym_bro_mobile.viewmodel;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;

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
    MutableLiveData<String> resultMessage = new MutableLiveData<>();

    public LiveData<String> getResultMessage() {
        return resultMessage;
    }

    public void login(String username, String password, Context context) {
        Log.d("LoginActivity", "in viewmodel 1");
        OkHttpClient client = new OkHttpClient();

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
                .url(context.getString(R.string.api_url) + "/login")
                .post(body)
                .build();

        Log.d("LoginActivity", "in viewmodel 2");

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                Log.d("LoginActivity", "in viewmodel 5");
                if (response.isSuccessful()) {
                    Log.d("LoginActivity", "in viewmodel 3");
                    try {
                        JSONObject resJson = new JSONObject(response.body().string());
                        String token = resJson.getString("jwt_token");
                        new JwtService(context).saveToken(token);
                        resultMessage.postValue("Login successful\nJWT: " + token);
                    } catch (JSONException e) {
                        resultMessage.postValue("Invalid response format");
                    }
                } else {
                    Log.d("LoginActivity", "in viewmodel 4");
                    resultMessage.postValue("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.d("LoginActivity", e.getMessage());
            }
        });
    }

    public void register(String username, String password, Context context) {
        // Implement similarly to login()
    }
}
