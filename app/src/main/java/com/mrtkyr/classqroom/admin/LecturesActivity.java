package com.mrtkyr.classqroom.admin;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;

public class LecturesActivity extends AppCompatActivity {
    private String userUUID;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        userUUID = getIntent().getStringExtra("USER_UUID");
        databaseHelper = new DatabaseHelper(this);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}
