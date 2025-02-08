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
import com.mrtkyr.classqroom.model.LecturerModel;

import java.util.List;

public class LecturersActivity extends AppCompatActivity {
    private String userUUID;
    private DatabaseHelper databaseHelper;
    private EditText editTextSearch;
    private TableLayout tableLayoutLecturers;
    private ActivityResultLauncher<Intent> addLecturerLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturers);

        userUUID = getIntent().getStringExtra("USER_UUID");
        databaseHelper = new DatabaseHelper(this);
        SearchView searchView = findViewById(R.id.searchViewLecturers);
        tableLayoutLecturers = findViewById(R.id.tableLayoutLecturers);
        TextView tvTotalLecturers = findViewById(R.id.tvTotalLecturers);
        Button btnAddLecturer = findViewById(R.id.btnAddLecturer);
        Button btnCancel= findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        btnAddLecturer.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddLecturerActivity.class);
            intent.putExtra("USER_UUID", userUUID);
            addLecturerLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        addLecturerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        tableLayoutLecturers.removeAllViews();
                        displayLecturers();
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

        displayLecturers();
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

    private void deleteLecturer(LecturerModel lecturer) {
        databaseHelper.deleteLecturer(lecturer.getUuid());
        tableLayoutLecturers.removeAllViews();
        displayLecturers();
    }

    public void displayLecturers() {
        List<LecturerModel> lecturers = databaseHelper.getAllLecturers();
        for (LecturerModel lecturer : lecturers) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView tvLecturerFullName = new TextView(this);
            String titleWithName = databaseHelper.getLecturerFullName(lecturer.getUuid());
            tvLecturerFullName.setText(titleWithName);
            tvLecturerFullName.setGravity(Gravity.CENTER);
            tvLecturerFullName.setPadding(12,16,12,16);
            tvLecturerFullName.setTextSize(12);
            tvLecturerFullName.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvLecturerFullName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            tvLecturerFullName.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.MATCH_PARENT, 2
            ));
            row.addView(tvLecturerFullName);

            View line = new View(this);
            line.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 5
            ));

            line.setBackgroundColor(ContextCompat.getColor(this, R.color.darkgray));
            TextView tvDepartment = new TextView(this);
            tvDepartment.setText(databaseHelper.getDepartmentNameById(lecturer.getDepartmentId()));
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
                for (int i = 0; i < tableLayoutLecturers.getChildCount(); i++) {
                    View child = tableLayoutLecturers.getChildAt(i);
                    child.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
                }
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            });

            row.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(this, row);
                popupMenu.getMenuInflater().inflate(R.menu.menu_departments, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menu_edit) {
//                        editLecturer(lecturer);
                        return true;
                    } else if (item.getItemId() == R.id.menu_delete) {
                        deleteLecturer(lecturer);
                        return true;
                    }
                    return false;
                });

                popupMenu.show();
                return true;
            });
            tableLayoutLecturers.addView(row);
            tableLayoutLecturers.addView(line);
        }
        updateTotalLecturers();
    }

    private void updateTotalLecturers() {
        TextView tvTotalLecturers = findViewById(R.id.tvTotalLecturers);
        int totalLecturers = databaseHelper.getAllLecturers().size();
        if (totalLecturers > 0) {
            tvTotalLecturers.setText(getString(R.string.totallecturers, String.valueOf(totalLecturers)));
        } else {
            tvTotalLecturers.setText(getString(R.string.nolecturer));
        }
    }
}
