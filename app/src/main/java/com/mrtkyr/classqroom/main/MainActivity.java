package com.mrtkyr.classqroom.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.SessionManager;

public class MainActivity extends AppCompatActivity {
    SwitchMaterial darkModeSwitch;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        int nightMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        SessionManager sessionManager = new SessionManager(MainActivity.this);
        String token = sessionManager.getToken();

        if (token != null && !token.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onClickLoginMain(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                    Activity.OVERRIDE_TRANSITION_OPEN,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
            );
        } else {
            //noinspection deprecation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
