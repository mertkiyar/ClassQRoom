package com.example.classqroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText edtMailLogin, edtPassLogin;
    TextView tvForgotPassword;
    Button btnCancelLogin;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMailLogin = findViewById(R.id.edtMailLogin);
        edtPassLogin = findViewById(R.id.edtPasswordLogin);

        btnCancelLogin = findViewById(R.id.btnCancelLogin);
        btnCancelLogin.setOnClickListener(v -> {
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

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    public void onClickLogin(View view) {
        String email = edtMailLogin.getText().toString().trim();
        String password = edtPassLogin.getText().toString().trim();
        UserModel userModel;

        if (dataBaseHelper != null) {
            try {
                if (!email.isEmpty() && !password.isEmpty()) {
                    userModel = new UserModel(email, password);
                    boolean isUserExist = dataBaseHelper.checkUser(userModel.getEmail());
                    if (isUserExist) {
                        boolean isCorrect = dataBaseHelper.authenticateUser(userModel.getEmail(), userModel.getPassword());
                        if (isCorrect) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.notregisteredemail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.errorregister) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (!edtMailLogin.getText().toString().isEmpty() && !edtPassLogin.getText().toString().isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }
}
