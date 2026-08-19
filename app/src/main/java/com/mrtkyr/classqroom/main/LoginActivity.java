package com.mrtkyr.classqroom.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.api.AuthApi;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.SessionManager;
import com.mrtkyr.classqroom.model.AuthRequest;
import com.mrtkyr.classqroom.model.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword;
    Button btnBack, btnLogin;
    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnBack = findViewById(R.id.btnBack);
        btnLogin = findViewById(R.id.btnLogin);
        pbLogin = findViewById(R.id.pbLogin);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                        Activity.OVERRIDE_TRANSITION_OPEN,
                        R.anim.slide_out_left,
                        R.anim.slide_in_right
                );
            } else {
                //noinspection deprecation
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    overrideActivityTransition(
                            Activity.OVERRIDE_TRANSITION_OPEN,
                            R.anim.slide_out_left,
                            R.anim.slide_in_right
                    );
                } else {
                    //noinspection deprecation
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }            }
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
                        String allowedChars = "!#$%^&*()_-+={}[]'.?";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && !allowedChars.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        tvForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                        Activity.OVERRIDE_TRANSITION_OPEN,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                );
            } else {
                //noinspection deprecation
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnLogin.setOnClickListener(this::onClickLogin);
    }

    public void onClickLogin(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.ERROR_FILL_BLANKS), Toast.LENGTH_SHORT).show();
            return;
        }

        pbLogin.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        AuthApi authApi = ApiClient.getClient(LoginActivity.this).create(AuthApi.class);
        SessionManager sessionManager = new SessionManager(LoginActivity.this);
        AuthRequest request = new AuthRequest(email, password);

        authApi.login(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                pbLogin.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveToken(response.body().getToken());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.ERROR_INVALID_CREDENTIAL), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                pbLogin.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, getString(R.string.ERROR_UNKNOWN) + ": " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}