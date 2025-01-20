package com.mrtkyr.classqroom.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.fragment.AddLecturerInfoFragment;
import com.mrtkyr.classqroom.fragment.AddLecturerPassFragment;
import com.mrtkyr.classqroom.model.LecturerModel;
import com.mrtkyr.classqroom.model.UserModel;
import com.mrtkyr.classqroom.main.RegisterActivity;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AddLecturerActivity extends AppCompatActivity implements AddLecturerInfoFragment.OnNextClickListener, AddLecturerPassFragment.OnAddClickListener {
    FragmentManager fragmentManager;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlecturer);

        databaseHelper = new DatabaseHelper(this);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            showAddLecturerInfoFragment();
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

    private void showAddLecturerInfoFragment() {
        AddLecturerInfoFragment addLecturerInfoFragment = new AddLecturerInfoFragment();
        addLecturerInfoFragment.setOnNextClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flAddLecturer, addLecturerInfoFragment)
                .commit();
    }

    private void showAddLecturerPassFragment(String name, String surname, String email, String department, String title) {
        AddLecturerPassFragment addLecturerPassFragment = new AddLecturerPassFragment(name, surname, email, department, title);
        addLecturerPassFragment.setOnAddClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flAddLecturer, addLecturerPassFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNextClicked(String name, String surname, String email, String department, String title) {
        UserModel userModel;
        if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !department.isEmpty()) {
            userModel = new UserModel(email);
            boolean isUserExist = databaseHelper.checkUser(userModel.getEmail());
            if (!isUserExist) {
                showAddLecturerPassFragment(name, surname, email, department, title);
            } else {
                Toast.makeText(this, getString(R.string.registeredemail), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddClicked(String name, String surname, String email, String department, String title, String password, String passwordConf) {
        UserModel userModel;
        LecturerModel lecturerModel;
        UUID uuid = RegisterActivity.generateUUID();
        if (databaseHelper != null) {
            try {
                if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !department.isEmpty() && !password.isEmpty() && !passwordConf.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String dateTimeFormatted = java.time.LocalDateTime.now().format(formatter);
                    if (password.equals(passwordConf)) {
                        userModel = new UserModel(uuid, name, surname, email, databaseHelper.hashPassword(password), databaseHelper.getDepartmentIdByName(department), "Lecturer", dateTimeFormatted, dateTimeFormatted);
                        lecturerModel = new LecturerModel(uuid, title, databaseHelper.getDepartmentIdByName(department), false);
                        boolean isUserAdded = databaseHelper.addNewUser(userModel);
                        boolean isLecturerAdded = databaseHelper.addNewLecturer(lecturerModel);
                        if (isUserAdded && isLecturerAdded) {
                            Toast.makeText(this, getString(R.string.addlecturersucces), Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            Toast.makeText(this, getString(R.string.erroraddlecturer), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.notsamepassword), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.errorregister) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
