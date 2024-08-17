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
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtSurname = findViewById(R.id.edtSurname);
        edtEmail = findViewById(R.id.edtEmail);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordConf = findViewById(R.id.edtPasswordConf);

        btnCancelRegister = findViewById(R.id.btnCancelRegister);
        btnCancelRegister.setOnClickListener(v -> finish());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    public void onClickRegister(View view) {
        String name = edtName.getText().toString();
        String surname = edtSurname.getText().toString();
        String email = edtEmail.getText().toString().trim();
        String studentNumber = edtStudentNumber.getText().toString();
        String password = edtPassword.getText().toString();
        String passwordConf = edtPasswordConf.getText().toString();

        if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !studentNumber.isEmpty() && !password.isEmpty() && !passwordConf.isEmpty()) {
            UserModel userModel;

            try {
                if (!name.equals("sad")) {
                    Log.d("Error $", "Error #4");
                    if (edtPassword.getText().toString().trim().equals(edtPasswordConf.getText().toString().trim())) {
                        userModel = new UserModel(-1, edtName.getText().toString(), edtSurname.getText().toString(), email,
                                Integer.parseInt(edtStudentNumber.getText().toString()), edtPassword.getText().toString());
                        boolean isSuccessful = dataBaseHelper.addNewUser(userModel);
                        if (isSuccessful) {
                            Toast.makeText(this, getString(R.string.registersucces), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        } else {
                            Toast.makeText(this, getString(R.string.registernotsuccess), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.notsamepassword), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.registeredemail), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exp) {
                Toast.makeText(this, getString(R.string.errorregister), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }
}