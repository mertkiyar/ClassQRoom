package com.mrtkyr.classqroom.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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
import com.mrtkyr.classqroom.model.DepartmentModel;

import java.util.List;

public class DepartmentsActivity extends AppCompatActivity {
    private String userUUID;
    private DatabaseHelper databaseHelper;
    private TableLayout tableLayoutDepartments;
    private ActivityResultLauncher<Intent> addDepartmentLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        userUUID = getIntent().getStringExtra("USER_UUID");
        databaseHelper = new DatabaseHelper(this);
        SearchView searchView = findViewById(R.id.searchViewDepartments);
        tableLayoutDepartments = findViewById(R.id.tableLayoutDepartments);
        TextView tvTotalDepartments = findViewById(R.id.tvTotalDepartments);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnAddDepartment = findViewById(R.id.btnAddDepartment);

        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        btnAddDepartment.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddDepartmentActivity.class);
            intent.putExtra("USER_UUID", userUUID);
            addDepartmentLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        addDepartmentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        tableLayoutDepartments.removeAllViews();
                        displayDepartments();
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

        displayDepartments();
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
//    private void editDepartment(DepartmentModel department) {
//        Intent intent = new Intent(this, EditDepartmentActivity.class);
//        intent.putExtra("DEPARTMENT_ID", department.getDepartmentId());
//        startActivity(intent);
//    } BURADA POPUP MENÜ AÇILACAK ŞEKİLDE DÜZENLE! lecturesactivity facultiesactivity lecturesactivity(eklenmedi)

    private void deleteDepartment(DepartmentModel department) {
        databaseHelper.deleteDepartment(department.getDepartmentId());
        tableLayoutDepartments.removeAllViews();
        displayDepartments();
    }

    public void displayDepartments() {
        List<DepartmentModel> departments = databaseHelper.getAllDepartments();
        for (DepartmentModel department : departments) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView tvDepartmentName = new TextView(this);
            tvDepartmentName.setText(department.getDepartmentName());
            tvDepartmentName.setGravity(Gravity.CENTER);
            tvDepartmentName.setPadding(12,16,12,16);
            tvDepartmentName.setTextSize(16);
            tvDepartmentName.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvDepartmentName.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            tvDepartmentName.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.MATCH_PARENT, 3
            ));
            row.addView(tvDepartmentName);

            View line = new View(this);
            line.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, 5
            ));

            line.setBackgroundColor(ContextCompat.getColor(this, R.color.darkgray));
            TextView tvDepartmentLanguage = new TextView(this);
            tvDepartmentLanguage.setText(department.getDepartmentLanguage());
            tvDepartmentLanguage.setGravity(Gravity.CENTER);
            tvDepartmentLanguage.setPadding(12,16,12,16);
            tvDepartmentLanguage.setTextSize(16);
            tvDepartmentLanguage.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvDepartmentLanguage.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            tvDepartmentLanguage.setLayoutParams(new TableRow.LayoutParams(
                    0, TableRow.LayoutParams.MATCH_PARENT, 2
            ));
            row.addView(tvDepartmentLanguage);

            row.setOnClickListener(v -> {
                for (int i = 0; i < tableLayoutDepartments.getChildCount(); i++) {
                    View child = tableLayoutDepartments.getChildAt(i);
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
                        deleteDepartment(department);
                        return true;
                    }
                    return false;
                });

                popupMenu.show();
                return true;
            });
            tableLayoutDepartments.addView(row);
            tableLayoutDepartments.addView(line);
        }
        updateTotalLecturers();
    }
    private void updateTotalLecturers() {
        TextView tvTotalDepartments = findViewById(R.id.tvTotalDepartments);
        int totalDepartments = databaseHelper.getAllDepartments().size();
        if (totalDepartments > 0) {
            tvTotalDepartments.setText(getString(R.string.totaldepartments, String.valueOf(totalDepartments)));
        } else {
            tvTotalDepartments.setText(getString(R.string.nodepartment));
        }
    }
}
