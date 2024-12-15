package com.example.classqroom;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword;
    Button btnCancel;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnCancel = findViewById(R.id.btnCancel);
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

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        edtEmail.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(40),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && character != '@' && character != '.') {
                            return "";
                        }
                    }
                    return null;
                }
        });
        edtPassword.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        String allowedChars = "~!@#$%^&*()_-+={}[]|:;\"'<,>.?/";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && !allowedChars.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    public void onClickLogin(View view) {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        UserModel userModel;

        if (databaseHelper != null) {
            try {
                if (!email.isEmpty() && !password.isEmpty()) {
                    userModel = new UserModel(email, password);
                    boolean isUserExist = databaseHelper.checkUser(userModel.getEmail());
                    if (isUserExist) {
                        boolean isCorrect = databaseHelper.authenticateUser(userModel.getEmail(), userModel.getPassword());
                        if (isCorrect) {
                            String type = databaseHelper.getUserType(userModel.getEmail());
                            if (type.equals("Student")) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                UUID uuid = databaseHelper.getUuid(email);
                                intent.putExtra("USER_UUID", uuid.toString());
                                intent.putExtra("USER_TYPE", "Student");
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else if (type.equals("Lecturer")) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                UUID uuid = databaseHelper.getUuid(email);
                                intent.putExtra("USER_UUID", uuid.toString());
                                intent.putExtra("USER_TYPE", "Lecturer");
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.wrongpassword), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.notregisteredemail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.errorlogin) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
