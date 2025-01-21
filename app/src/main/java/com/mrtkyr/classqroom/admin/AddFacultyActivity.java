package com.mrtkyr.classqroom.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.model.FacultyModel;

public class AddFacultyActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText edtFacultyName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfaculty);

        databaseHelper = new DatabaseHelper(this);
        String userUUID = getIntent().getStringExtra("USER_UUID");
        edtFacultyName = findViewById(R.id.edtFacultyName);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnAddFaculty = findViewById(R.id.btnAddFaculty);

        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void onClickAddFaculty(View view) {
        String facultyName = edtFacultyName.getText().toString().trim();
        FacultyModel facultyModel;

        if (databaseHelper != null) {
            try {
                if (!facultyName.isEmpty()) {
                    boolean isExist = databaseHelper.checkFaculty(facultyName);
                    if (!isExist) {
                        facultyModel = new FacultyModel(facultyName);
                        boolean isAdded = databaseHelper.addNewFaculty(facultyModel);
                        if (isAdded) {
                            setResult(RESULT_OK);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            Toast.makeText(this, getString(R.string.erroraddfaculty), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.facultyalreadyexist), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.erroraddfaculty) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
