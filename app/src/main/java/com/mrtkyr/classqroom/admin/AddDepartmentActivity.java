package com.mrtkyr.classqroom.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.model.DepartmentModel;

public class AddDepartmentActivity extends AppCompatActivity {
    private EditText edtDepartmentName;
    private Spinner spinDepartmentLanguage;
    Button btnAddDepartment, btnCancelDepartment;
    private boolean isSelectedDepartmentLanguage = false;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddepartment);

        databaseHelper = new DatabaseHelper(this);
        String userUUID = getIntent().getStringExtra("USER_UUID");
        edtDepartmentName = findViewById(R.id.edtDepartmentName);
        spinDepartmentLanguage = findViewById(R.id.spinDepartmentLanguage);
        btnAddDepartment = findViewById(R.id.btnAddDepartment);
        btnCancelDepartment = findViewById(R.id.btnCancelDepartment);

        String[] languages = new String[] {
                getString(R.string.selectlang), getString(R.string.english), getString(R.string.turkish),
                getString(R.string.german), getString(R.string.french), getString(R.string.italian),
                getString(R.string.spanish), getString(R.string.portuguese), getString(R.string.russian)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDepartmentLanguage.setAdapter(adapter);
        spinDepartmentLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    isSelectedDepartmentLanguage = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnCancelDepartment.setOnClickListener(v -> {
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

    public void onClickAddDepartment(View view) {
        String departmentName = edtDepartmentName.getText().toString().trim();
        String departmentLanguage = spinDepartmentLanguage.getSelectedItem().toString();
        DepartmentModel departmentModel;

        if (databaseHelper != null) {
            try {
                if (!departmentName.isEmpty() && isSelectedDepartmentLanguage) {
                    boolean isExist = databaseHelper.checkDepartment(departmentName, departmentLanguage);
                    if (!isExist) {
                        departmentModel = new DepartmentModel(departmentName, departmentLanguage);
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
                    Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.erroradddepartment) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
