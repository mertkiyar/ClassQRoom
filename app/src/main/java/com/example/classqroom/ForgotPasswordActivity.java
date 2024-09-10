package com.example.classqroom;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtEmail, edtStudentNumber;
    Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        edtEmail = findViewById(R.id.edtEmail);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);
        btnCancel= findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
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
    }

    public void onClickReset(View view) {
        if (!edtEmail.getText().toString().isEmpty() || !edtStudentNumber.getText().toString().isEmpty()) {
            Toast.makeText(this,getString(R.string.sentresetcode), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }
}
