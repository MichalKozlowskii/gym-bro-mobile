package com.example.gym_bro_mobile.di;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ContextModule {

    @Provides
    public static Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}