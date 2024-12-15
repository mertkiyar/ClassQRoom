package com.example.classqroom;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements UserInfoFragment.OnNextClickListener, PasswordFragment.OnRegisterClickListener {

    private FragmentManager fragmentManager;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            showUserInfoFragment();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
    }

    private void showUserInfoFragment() {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        userInfoFragment.setOnNextClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flRegister, userInfoFragment)
                .commit();
    }

    private void showPasswordFragment(String name, String surname, String email, int studentNumber) {
        PasswordFragment passwordFragment = new PasswordFragment(name, surname, email, studentNumber);
        passwordFragment.setOnRegisterClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flRegister, passwordFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNextClicked(String name, String surname, String email, int studentNumber) {
        UserModel userModel;
        if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && studentNumber != -1) {
            userModel = new UserModel(email);
            boolean isUserExist = databaseHelper.checkUser(userModel.getEmail());
            if (!isUserExist) {
                showPasswordFragment(name, surname, email, studentNumber);
            } else {
                Toast.makeText(this, getString(R.string.registeredemail), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }
    public UUID generateUUID() {
        return UUID.randomUUID();
    }

    @Override
    public void onRegisterClicked(String name, String surname, String email, int studentNumber, String password, String passwordConf) {
        UserModel userModel;
        StudentModel studentModel;
        UUID uuid = generateUUID();
        if (databaseHelper != null) {
            try {
                if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && studentNumber != -1  && !password.isEmpty() && !passwordConf.isEmpty()) {
                    if (password.equals(passwordConf)) {
                        userModel = new UserModel(uuid, name, surname, email, databaseHelper.hashPassword(password), 0, "Student");
                        studentModel = new StudentModel(uuid, studentNumber, 1, false);
                        if (name.equals("ADMIN") && surname.equals("ADMIN") && email.equals("admin@classqrroom.com") && studentNumber==6378) {
                            userModel = new UserModel(uuid, name, surname, email, databaseHelper.hashPassword(password), -1, "Admin");
                        }                                                                           // for testing purpose
                        boolean isUserAdded = databaseHelper.addNewUser(userModel);
                        boolean isStudentAdded = databaseHelper.addNewStudent(studentModel);
                        if (isUserAdded && isStudentAdded) {
                            Toast.makeText(this, getString(R.string.registersucces), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            intent.putExtra("USER_UUID", uuid.toString());
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Toast.makeText(this, getString(R.string.registernotsuccess), Toast.LENGTH_SHORT).show();
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
            Log.e("RegisterActivity", "databaseHelper is null");
        }
    }
}
