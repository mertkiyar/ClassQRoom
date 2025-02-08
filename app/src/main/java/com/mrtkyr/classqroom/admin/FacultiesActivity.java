package com.mrtkyr.classqroom.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.mrtkyr.classqroom.model.FacultyModel;

import java.util.List;

public class FacultiesActivity extends AppCompatActivity {
    private String userUUID;
    private DatabaseHelper databaseHelper;
    private TableLayout tableLayoutFaculties;
    private ActivityResultLauncher<Intent> addFacultyLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties);

        userUUID = getIntent().getStringExtra("USER_UUID");
        databaseHelper = new DatabaseHelper(this);
        SearchView searchView = findViewById(R.id.searchViewFaculties);
        tableLayoutFaculties = findViewById(R.id.tableLayoutFaculties);
        TextView tvTotalFaculties = findViewById(R.id.tvTotalFaculties);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnAddFaculty = findViewById(R.id.btnAddFaculty);

        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        btnAddFaculty.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFacultyActivity.class);
            intent.putExtra("USER_UUID", userUUID);
            addFacultyLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        addFacultyLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        tableLayoutFaculties.removeAllViews();
                        displayFaculties();
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

        displayFaculties();
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

//    private void editFaculty(FacultyModel faculty) {
//        Intent intent = new Intent(this, EditFacultyActivity.class);
//        intent.putExtra("FACULTY_ID", faculty.getFacultyId());
//        startActivity(intent);
//    }

    private void deleteFaculty(FacultyModel faculty) {
        databaseHelper.deleteFaculty(faculty.getFacultyId());
        tableLayoutFaculties.removeAllViews();
        displayFaculties();
    }

    public void displayFaculties() {
        List<FacultyModel> faculties = databaseHelper.getAllFaculties();

        for (FacultyModel faculty : faculties) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView tvFacultyName = new TextView(this);
            tvFacultyName.setText(faculty.getFacultyName());
            tvFacultyName.setPadding(12,16,12,16);
            tvFacultyName.setTextSize(16);
            tvFacultyName.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvFacultyName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            tvFacultyName.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.MATCH_PARENT, 1
            ));
            row.addView(tvFacultyName);

            View line = new View(this);
            line.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 5
            ));

            line.setBackgroundColor(ContextCompat.getColor(this, R.color.darkgray));

            row.setOnClickListener( v -> {
                for (int i = 0; i < tableLayoutFaculties.getChildCount(); i++) {
                    View child = tableLayoutFaculties.getChildAt(i);
                    child.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
                }
                row.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
            });

            row.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(this, row);
                popupMenu.getMenuInflater().inflate(R.menu.menu_departments, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menu_edit) {
//                        editDepartment(department);
                        return true;
                    } else if (item.getItemId() == R.id.menu_delete) {
                        deleteFaculty(faculty);
                        return true;
                    }
                    return false;
                });

                popupMenu.show();
                return true;
            });
            tableLayoutFaculties.addView(row);
            tableLayoutFaculties.addView(line);
            TextView tvTotalFaculties = findViewById(R.id.tvTotalFaculties);
            if (!faculties.isEmpty()) {
                tvTotalFaculties.setText(getString(R.string.totalfaculties, String.valueOf(faculties.size())));
            } else {
                tvTotalFaculties.setText(getString(R.string.nofaculty));
            }
        }
        updateTotalFaculties();
    }
    private void updateTotalFaculties() {
        TextView tvTotalFaculties = findViewById(R.id.tvTotalFaculties);
        int totalFaculties = databaseHelper.getAllFaculties().size();
        if (totalFaculties > 0) {
            tvTotalFaculties.setText(getString(R.string.totalfaculties, String.valueOf(totalFaculties)));
        } else {
            tvTotalFaculties.setText(getString(R.string.nofaculty));
        }
    }
}
