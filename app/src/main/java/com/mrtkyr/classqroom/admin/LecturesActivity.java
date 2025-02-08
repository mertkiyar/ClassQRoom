package com.mrtkyr.classqroom.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.model.LectureModel;

import java.util.List;

public class LecturesActivity extends AppCompatActivity {
    private String userUUID;
    private DatabaseHelper databaseHelper;
    private EditText editTextSearch;
    private TableLayout tableLayoutLectures;
    private ActivityResultLauncher<Intent> addLectureLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);

        userUUID = getIntent().getStringExtra("USER_UUID");
        databaseHelper = new DatabaseHelper(this);
        SearchView searchView = findViewById(R.id.searchViewLectures);
        tableLayoutLectures = findViewById(R.id.tableLayoutLectures);
        TextView tvTotalLectures = findViewById(R.id.tvTotalLectures);
        Button btnAddLecture = findViewById(R.id.btnAddLecture);
        Button btnCancel= findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        btnAddLecture.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLectureActivity.class);
            intent.putExtra("USER_UUID", userUUID);
            addLectureLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        addLectureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        tableLayoutLectures.removeAllViews();
                        displayLectures();
                    }
                }
        );

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        displayLectures();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteLecture(LectureModel lecture) {
        databaseHelper.deleteLecture(String.valueOf(lecture.getLectureId()));
        tableLayoutLectures.removeAllViews();
        displayLectures();
    }

    public void displayLectures() {
        List<LectureModel> lectures = databaseHelper.getAllLectures();
        for (LectureModel lecture : lectures) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView tvLectureName = new TextView(this);
            tvLectureName.setText(lecture.getLectureName());
            tvLectureName.setGravity(Gravity.CENTER);
            tvLectureName.setPadding(12,16,12,16);
            tvLectureName.setTextSize(12);
            tvLectureName.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvLectureName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            tvLectureName.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.MATCH_PARENT, 2
            ));
            row.addView(tvLectureName);

            View line = new View(this);
            line.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 5
            ));

            line.setBackgroundColor(ContextCompat.getColor(this, R.color.darkgray));
            TextView tvDepartment = new TextView(this);
            tvDepartment.setText(databaseHelper.getLecturerFullName(lecture.getLecturerUUID()));
            tvDepartment.setGravity(Gravity.CENTER);
            tvDepartment.setPadding(12,16,12,16);
            tvDepartment.setTextSize(12);
            tvDepartment.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvDepartment.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            tvDepartment.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.MATCH_PARENT, 3
            ));
            row.addView(tvDepartment);

            row.setOnClickListener(v -> {
                for (int i = 0; i < tableLayoutLectures.getChildCount(); i++) {
                    View child = tableLayoutLectures.getChildAt(i);
                    child.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
                }
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            });

            row.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(this, row);
                popupMenu.getMenuInflater().inflate(R.menu.menu_departments, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menu_edit) {
//                        editLecture(lecturer);
                        return true;
                    } else if (item.getItemId() == R.id.menu_delete) {
                        deleteLecture(lecture);
                        return true;
                    }
                    return false;
                });

                popupMenu.show();
                return true;
            });
            tableLayoutLectures.addView(row);
            tableLayoutLectures.addView(line);
        }
        updateTotalLectures();
    }

    private void updateTotalLectures() {
        TextView tvTotalLecturers = findViewById(R.id.tvTotalLectures);
        int totalLecturers = databaseHelper.getAllLectures().size();
        if (totalLecturers > 0) {
            tvTotalLecturers.setText(getString(R.string.totallectures, String.valueOf(totalLecturers)));
        } else {
            tvTotalLecturers.setText(getString(R.string.nolecture));
        }
    }
}
