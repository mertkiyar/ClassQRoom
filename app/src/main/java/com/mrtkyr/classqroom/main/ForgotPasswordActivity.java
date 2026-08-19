package com.mrtkyr.classqroom.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.SessionManager;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtEmail, edtStudentNumber, edtNewPassword, edtConfirmPassword;
    Button btnCancel, btnNext, btnBack, btnResetPassword;
    TextInputLayout tfEmail, tfStudentNumber, tfNewPassword, tfConfirmPassword;
    LinearLayout llbuttons, llotherbuttons;
    ProgressBar pbForgotPassword;
    Boolean isInPasswordTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        SessionManager sessionManager = new SessionManager(ForgotPasswordActivity.this);
        String authToken = sessionManager.getToken();

        edtEmail = findViewById(R.id.edtEmail);
        edtStudentNumber = findViewById(R.id.edtStudentNumber);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tfEmail = findViewById(R.id.tfEmail);
        tfStudentNumber = findViewById(R.id.tfStudentNumber);
        tfNewPassword = findViewById(R.id.tfNewPassword);
        tfConfirmPassword = findViewById(R.id.tfConfirmPassword);
        llbuttons = findViewById(R.id.llbuttons);
        llotherbuttons = findViewById(R.id.llotherbuttons);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        pbForgotPassword = findViewById(R.id.pbForgotPassword);
        isInPasswordTheme = false;

        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getQueryParameter("oobCode") != null) {
            tfEmail.setVisibility(View.GONE);
            tfStudentNumber.setVisibility(View.GONE);
            llbuttons.setVisibility(View.GONE);
            tfNewPassword.setVisibility(View.VISIBLE);
            tfConfirmPassword.setVisibility(View.VISIBLE);
            llotherbuttons.setVisibility(View.VISIBLE);
            isInPasswordTheme = true;

            btnResetPassword.setOnClickListener(this::onClickReset);

        } else {
            tfEmail.setVisibility(View.GONE);
            tfStudentNumber.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            
            TextView tvDisabledMessage = findViewById(R.id.tvDisabledMessage);
            if (tvDisabledMessage != null) {
                tvDisabledMessage.setVisibility(View.VISIBLE);
            }

            llbuttons.setVisibility(View.VISIBLE);
            tfNewPassword.setVisibility(View.GONE);
            tfConfirmPassword.setVisibility(View.GONE);
            llotherbuttons.setVisibility(View.GONE);
            isInPasswordTheme = false;

            // btnNext.setOnClickListener(v -> sendEmail());
        }

        btnCancel.setOnClickListener(v -> {
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

        btnBack.setOnClickListener(v -> {
            tfEmail.setVisibility(View.VISIBLE);
            tfStudentNumber.setVisibility(View.VISIBLE);
            llbuttons.setVisibility(View.VISIBLE);
            tfNewPassword.setVisibility(View.GONE);
            tfConfirmPassword.setVisibility(View.GONE);
            llotherbuttons.setVisibility(View.GONE);
            isInPasswordTheme = false;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isInPasswordTheme) {
                    tfEmail.setVisibility(View.VISIBLE);
                    tfStudentNumber.setVisibility(View.VISIBLE);
                    llbuttons.setVisibility(View.VISIBLE);
                    tfNewPassword.setVisibility(View.GONE);
                    tfConfirmPassword.setVisibility(View.GONE);
                    llotherbuttons.setVisibility(View.GONE);
                    isInPasswordTheme = false;
                } else {
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
                }
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
        edtNewPassword.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        String allowedChars = "!@#$%^&*()_-+={}[]'<,>.?";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && !allowedChars.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    public void onClickNext(View view) {
        String email = edtEmail.getText().toString().trim();
        String studentNumber = edtStudentNumber.getText().toString().trim();
        if (!email.isEmpty() && !studentNumber.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tfEmail.setVisibility(View.GONE);
                tfStudentNumber.setVisibility(View.GONE);
                llbuttons.setVisibility(View.GONE);
                tfNewPassword.setVisibility(View.VISIBLE);
                tfConfirmPassword.setVisibility(View.VISIBLE);
                llotherbuttons.setVisibility(View.VISIBLE);
                isInPasswordTheme = true;
                edtNewPassword.requestFocus();
            } else {
                edtEmail.setError(getString(R.string.ERROR_INVALID_EMAIL));
                edtEmail.requestFocus();
            }

        } else {
            if (edtEmail.getText().toString().trim().isEmpty()) {
                edtEmail.setError(getString(R.string.ERROR_FILL_BLANKS));
                edtEmail.requestFocus();
            }
            if (edtStudentNumber.getText().toString().trim().isEmpty()) {
                edtStudentNumber.setError(getString(R.string.ERROR_FILL_BLANKS));
                edtStudentNumber.requestFocus();
            }
        }
    }

    public void sendEmail() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.ERROR_FILL_BLANKS));
            edtEmail.requestFocus();
            return;
        }

        pbForgotPassword.setVisibility(View.VISIBLE);
        btnNext.setEnabled(false);

//        ActionCodeSettings actionCodeSettings =
//                ActionCodeSettings.newBuilder()
//                        .setUrl("https://classqroom.mrtkyr.com")
//                        .setHandleCodeInApp(true)
//                        .setAndroidPackageName(
//                                getPackageName(),
//                                true,
//                                null
//                        )
//                        .build();
//
//        auth.sendPasswordResetEmail(email, actionCodeSettings)
//                .addOnCompleteListener(task -> {
//                    pbForgotPassword.setVisibility(View.GONE);
//                    btnNext.setEnabled(true);
//
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, getString(R.string.MSG_SENT_RESET_LINK_TO_EMAIL), Toast.LENGTH_LONG).show();
//                    }
//                });
    }

    public void onClickReset(View view) {
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this,getString(R.string.ERROR_FILL_BLANKS), Toast.LENGTH_SHORT).show();
            if (newPassword.isEmpty()) {
                edtNewPassword.requestFocus();
            } else {
                edtConfirmPassword.requestFocus();
            }
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this,getString(R.string.ERROR_SHORT_PASSWORD), Toast.LENGTH_SHORT).show();
            edtNewPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this,getString(R.string.ERROR_PASSWORDS_NOT_MATCH), Toast.LENGTH_SHORT).show();
            edtNewPassword.requestFocus();
            return;
        }

        pbForgotPassword.setVisibility(View.VISIBLE);
        btnResetPassword.setEnabled(false);

//        auth.confirmPasswordReset(oobCode, newPassword).addOnCompleteListener(task -> {
//            pbForgotPassword.setVisibility(View.GONE);
//            btnResetPassword.setEnabled(true);
//
//            if (task.isSuccessful()) {
//                Toast.makeText(this, getString(R.string.MSG_SUCCESS_FORGOT_PASSWORD), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            } else {
//                Toast.makeText(this, getString(R.string.ERROR_NOT_CHANGE_PASSWORD), Toast.LENGTH_LONG).show();
//            }
//        });
    }
}
