package com.mrtkyr.classqroom.admin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;

public class LecturersActivity extends AppCompatActivity {
    private String userUUID;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturers);
        userUUID = getIntent().getStringExtra("USER_UUID");
        databaseHelper = new DatabaseHelper(this);

    }
}
