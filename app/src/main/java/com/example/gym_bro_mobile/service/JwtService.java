package com.example.gym_bro_mobile.service;

import android.content.Context;
import android.content.SharedPreferences;

public class JwtService {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_JWT = "jwt_token";
    private final SharedPreferences prefs;

    public JwtService(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_JWT, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_JWT, null);
    }

    public void clearToken() {
        prefs.edit().remove(KEY_JWT).apply();
    }

    public boolean hasToken() {
        return getToken() != null;
    }
}
