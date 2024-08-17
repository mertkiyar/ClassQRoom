package com.example.classqroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText edtName, edtSurname, edtEmail, edtStudentNumber, edtPassword, edtPasswordConf;
    Button btnCancelRegister;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dataBaseHelper = new DataBaseHelper(this);

        edtName = findViewById(R.id.edtName);
        edtSurname = findViewById(R.id.edtSurname);
        edtEmail = findViewById(R.id.edtEmail);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordConf = findViewById(R.id.edtPasswordConf);

        btnCancelRegister = findViewById(R.id.btnCancelRegister);
        btnCancelRegister.setOnClickListener(v -> {
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
    public void onClickRegister(View view) {
        String name = edtName.getText().toString().trim();
        String surname = edtSurname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String studentNumber = edtStudentNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String passwordConf = edtPasswordConf.getText().toString().trim();
        UserModel userModel;

        if (dataBaseHelper != null) {
            try {
                if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !studentNumber.isEmpty() && !password.isEmpty() && !passwordConf.isEmpty()) {
                    if (password.equals(passwordConf)) {
                        userModel = new UserModel(-1, name, surname, email, Integer.parseInt(studentNumber), password);
                        boolean isUserExist = dataBaseHelper.checkUser(userModel.getEmail());
                        if (!isUserExist) {
                            boolean isUserAdded = dataBaseHelper.addNewUser(userModel);
                            if (isUserAdded) {
                                Toast.makeText(this, getString(R.string.registersucces), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                Toast.makeText(this, getString(R.string.registernotsuccess), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.registeredemail), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.notsamepassword), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.errorregister) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("RegisterActivity", "DataBaseHelper is null");
        }
    }
}