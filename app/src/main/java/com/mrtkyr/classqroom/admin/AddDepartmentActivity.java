package com.mrtkyr.classqroom.admin;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.model.DepartmentModel;
import com.mrtkyr.classqroom.model.FacultyModel;

import java.util.ArrayList;
import java.util.List;

public class AddDepartmentActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText edtDepartmentName, edtDepartmentCode;
    private Spinner spinDepartmentLanguage, spinDepartmentFaculty;
    Button btnAddDepartment, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddepartment);

        databaseHelper = new DatabaseHelper(this);
        String userUUID = getIntent().getStringExtra("USER_UUID");
        edtDepartmentName = findViewById(R.id.edtDepartmentName);
        edtDepartmentCode = findViewById(R.id.edtDepartmentCode);
        spinDepartmentLanguage = findViewById(R.id.spinDepartmentLanguage);
        spinDepartmentFaculty = findViewById(R.id.spinDepartmentFaculty);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddDepartment = findViewById(R.id.btnAddDepartment);

        String[] languages = new String[] {
                getString(R.string.selectlang), getString(R.string.english), getString(R.string.turkish),
                getString(R.string.german), getString(R.string.french), getString(R.string.italian),
                getString(R.string.spanish), getString(R.string.portuguese), getString(R.string.russian)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDepartmentLanguage.setAdapter(adapter);

        List<FacultyModel> faculties = databaseHelper.getAllFaculties();
        List<String> facultyNames = new ArrayList<>();
        facultyNames.add(getString(R.string.selectfaculty));
        for (FacultyModel faculty : faculties) {
            facultyNames.add(faculty.getFacultyName());
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facultyNames);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDepartmentFaculty.setAdapter(adapter1);

        if (faculties.isEmpty()) {
            spinDepartmentFaculty.setEnabled(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.firstfaculty))
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    })
                    .setCancelable(false)
                    .show();
        }

        btnCancel.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });


        edtDepartmentName.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(48),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        if (!Character.isLetter(character) && !Character.isSpaceChar(character) &&
                        !(character == '-') && !(character == '(') && !(character == ')')) {
                            return "";
                        }
                        if (Character.isSpaceChar(character)) {
                            if (dstart > 0 && Character.isSpaceChar(dest.charAt(dstart - 1))) {
                                return "";
                            }
                        }
                    }
                    return null;
                }
        });

        edtDepartmentCode.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(4),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        if (!Character.isLetter(character)) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    public void onClickAddDepartment(View view) {
        String departmentName = edtDepartmentName.getText().toString().trim();
        String departmentCode = edtDepartmentCode.getText().toString().trim();
        String departmentLanguage = spinDepartmentLanguage.getSelectedItem().toString();
        String departmentFaculty = spinDepartmentFaculty.getSelectedItem().toString();
        DepartmentModel departmentModel;

        if (databaseHelper != null) {
            try {
                if (!departmentName.isEmpty() && !departmentCode.isEmpty() && !departmentLanguage.equals(getString(R.string.selectlang))
                        && !departmentFaculty.equals(getString(R.string.selectfaculty))) {
                    if (departmentName.length() >= 3) {
                        boolean isExist = databaseHelper.checkDepartment(departmentName);
                        if (!isExist) {
                            departmentModel = new DepartmentModel(departmentName, departmentCode, departmentLanguage,
                                    databaseHelper.getFacultyIdByName(departmentFaculty));
                            boolean isAdded = databaseHelper.addNewDepartment(departmentModel);
                            if (isAdded) {
                                setResult(RESULT_OK);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else {
                                Toast.makeText(this, getString(R.string.erroradddepartment), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.departmentalreadyexist), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.departmentnamelength, "3"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.erroradddepartment) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
