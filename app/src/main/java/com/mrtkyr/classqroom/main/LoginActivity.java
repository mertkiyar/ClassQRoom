package com.mrtkyr.classqroom.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
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
//    private FirebaseAuth auth;
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword;
    Button btnBack, btnLogin;
    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        auth = FirebaseAuth.getInstance();
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
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

        authApi.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
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
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                pbLogin.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, getString(R.string.ERROR_UNKNOWN) + ": " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
//        auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    pbLogin.setVisibility(View.GONE);
//                    btnLogin.setEnabled(true);
//
//                    if (task.isSuccessful()) {
//                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        assert auth.getCurrentUser() != null;
//                        intent.putExtra("USER_UID", auth.getCurrentUser().getUid());
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        String errorMessage;
//                        try {
//                            throw Objects.requireNonNull(task.getException());
//                        } catch (FirebaseAuthException e) {
//                            String errorCode = e.getErrorCode();
//                            errorMessage = switch (errorCode) {
//                                case "ERROR_INVALID_EMAIL" ->
//                                        getString(R.string.ERROR_INVALID_EMAIL);
//                                case "ERROR_USER_NOT_FOUND" ->
//                                        getString(R.string.ERROR_USER_NOT_FOUND);
//                                case "ERROR_WRONG_PASSWORD" ->
//                                        getString(R.string.ERROR_WRONG_PASSWORD);
//                                case "ERROR_USER_DISABLED" ->
//                                        getString(R.string.ERROR_USER_DISABLED);
//                                case "ERROR_TOO_MANY_REQUESTS" ->
//                                        getString(R.string.ERROR_TOO_MANY_REQUESTS);
//                                case "ERROR_INVALID_CREDENTIAL" ->
//                                        getString(R.string.ERROR_INVALID_CREDENTIAL);
//                                default -> getString(R.string.ERROR_UNKNOWN) + ": " + errorCode;
//                            };
//                        } catch (Exception e) {
//                            errorMessage = getString(R.string.ERROR_UNEXPECTED) + e.getMessage();
//                        }
//                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
//                    }
//        });
    }
}