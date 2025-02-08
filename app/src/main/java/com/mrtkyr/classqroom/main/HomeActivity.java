package com.mrtkyr.classqroom.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.admin.DepartmentsActivity;
import com.mrtkyr.classqroom.admin.FacultiesActivity;
import com.mrtkyr.classqroom.admin.LecturersActivity;
import com.mrtkyr.classqroom.admin.LecturesActivity;
import com.mrtkyr.classqroom.lecturer.QRCodeCreatorActivity;
import com.mrtkyr.classqroom.student.QRCodeScannerActivity;
import com.mrtkyr.classqroom.R;

public class HomeActivity extends AppCompatActivity {
    private String userType;
    private String userUUID;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userType = getIntent().getStringExtra("USER_TYPE");
        databaseHelper = new DatabaseHelper(this);
        if (userType == null) {
            userType = "Student";
        }

        switch (userType) {
            case "Student":
                setContentView(R.layout.activity_home);
                break;
            case "Lecturer":
                setContentView(R.layout.activity_home_lecturer);
                break;
            case "Admin":
                setContentView(R.layout.activity_home_admin);
                break;
        }

        try (DatabaseHelper db = new DatabaseHelper(this)) {
            userUUID = getIntent().getStringExtra("USER_UUID");
            if (userUUID == null) {
                Toast.makeText(this, R.string.usernotfound, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            TextView tvWelcome = findViewById(R.id.tvWelcome);
            String nameFromDb = db.getUserNameFromUUID(userUUID);
            tvWelcome.setText(getString(R.string.welcome, nameFromDb));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void onBtnQRCodeClick(View view) {
        Intent intent;
        if (userType != null && userType.equals("Lecturer")) {
            intent = new Intent(this, QRCodeCreatorActivity.class);
        } else if (userType != null && userType.equals("Student")) {
            intent = new Intent(this, QRCodeScannerActivity.class);
        } else if (userType != null && userType.equals("Admin")){
            intent = new Intent(this, QRCodeScannerActivity.class);
        } else {
            intent = new Intent(this, QRCodeScannerActivity.class);
        }
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnNFCClick(View view) {
        Intent intent = new Intent(this, LecturersActivity.class);
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnClassCodeClick(View view) {
        Intent intent = switch (userType) {
            case "Lecturer" -> new Intent(this, QRCodeCreatorActivity.class);
            case "Admin" -> new Intent(this, DepartmentsActivity.class);
            default -> new Intent(this, QRCodeScannerActivity.class);
        };
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnAttendHistoryClick(View view) {
        Intent intent = new Intent(this, FacultiesActivity.class);
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnProfileClick(View view) {
        Intent intent = new Intent(this, LecturesActivity.class);
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnSettingsClick(View view) {
        Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBtnSupportClick(View view) {
        Toast.makeText(this, "Support Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.confirm));
            builder.setMessage(getString(R.string.logoutmessage))
                    .setPositiveButton(getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(R.string.no), dialogClickListener)
                    .show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
