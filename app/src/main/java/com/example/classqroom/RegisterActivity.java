package com.example.classqroom;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class RegisterActivity extends AppCompatActivity implements UserInfoFragment.OnNextClickListener, PasswordFragment.OnRegisterClickListener {

    private FragmentManager fragmentManager;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dataBaseHelper = new DataBaseHelper(this);
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
                .replace(R.id.frameLayout, userInfoFragment)
                .commit();
    }

    private void showPasswordFragment(String name, String surname, String email, String studentNumber) {
        PasswordFragment passwordFragment = new PasswordFragment(name, surname, email, studentNumber);
        passwordFragment.setOnRegisterClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.frameLayout, passwordFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNextClicked(String name, String surname, String email, String studentNumber) {
        UserModel userModel;
        if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !studentNumber.isEmpty()) {
            userModel = new UserModel(-1, name, surname, email, Integer.parseInt(studentNumber), null);
            boolean isUserExist = dataBaseHelper.checkUser(userModel.getEmail());
            if (!isUserExist) {
                showPasswordFragment(name, surname, email, studentNumber);
            } else {
                Toast.makeText(this, getString(R.string.registeredemail), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegisterClicked(String name, String surname, String email, String studentNumber, String password, String passwordConf) {
        UserModel userModel;

        if (dataBaseHelper != null) {
            try {
                if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !studentNumber.isEmpty() && !password.isEmpty() && !passwordConf.isEmpty()) {
                    if (password.equals(passwordConf)) {
                        userModel = new UserModel(-1, name, surname, email, Integer.parseInt(studentNumber), password);
                        boolean isUserAdded = dataBaseHelper.addNewUser(userModel);
                        if (isUserAdded) {
                            Toast.makeText(this, getString(R.string.registersucces), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
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
            Log.e("RegisterActivity", "DataBaseHelper is null");
        }
    }
}
