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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMailLogin = findViewById(R.id.edtMailLogin);
        edtPassLogin = findViewById(R.id.edtPasswordLogin);

        btnCancelLogin = findViewById(R.id.btnCancelLogin);
        btnCancelLogin.setOnClickListener(v -> finish());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        //TODO bu ve alttakini ileride sil. Hızlı giriş için.
        edtMailLogin.setText("1");
        edtPassLogin.setText("1");
    }

    public void onClickLogin(View view) {
        UserModel userModel = new UserModel(edtMailLogin.getText().toString(), edtPassLogin.getText().toString());
        if (!edtMailLogin.getText().toString().isEmpty() && !edtPassLogin.getText().toString().isEmpty()) {
            Toast.makeText(this, userModel.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }
}
