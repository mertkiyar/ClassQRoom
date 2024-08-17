package com.example.classqroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtMailForgotPass, edtStudentNumber;
    Button btnCancelForgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        btnCancelForgotPass = findViewById(R.id.btnCancelForgotPass);
        btnCancelForgotPass.setOnClickListener(v -> {
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

    public void onClickReset(View view) {
        edtMailForgotPass = findViewById(R.id.edtMailForgotPass);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);

        if (!edtMailForgotPass.getText().toString().isEmpty() || !edtStudentNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Password reset code has been sent! Please check your mail.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            Toast.makeText(this, "Please fill all the blanks!", Toast.LENGTH_SHORT).show();
        }
    }
}
