package com.example.RealFilm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.RealFilm.utils.Constants;

public class MyApplication extends Application {
    private static SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "MyPrefs";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.ACCESS_TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, null);
    }
}
