package com.mrtkyr.classqroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SessionManager {
    private SharedPreferences sharedPreferences;

    @SuppressWarnings("deprecation")
    public SessionManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    "secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e("SessionManager", "Error initializing EncryptedSharedPreferences", e);
        }
    }


    public void saveToken(String token) {
        sharedPreferences.edit().putString("JWT_TOKEN", token).apply();
    }
    public String getToken() {
        return sharedPreferences.getString("JWT_TOKEN", null);
    }
    public void removeToken() {
        sharedPreferences.edit().remove("JWT_TOKEN").apply();
    }
}
