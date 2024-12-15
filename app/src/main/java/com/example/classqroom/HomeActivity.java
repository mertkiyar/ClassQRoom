package com.example.classqroom;

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

public class HomeActivity extends AppCompatActivity {
    private String userType;
    private String userUUID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userType = getIntent().getStringExtra("USER_TYPE");

        if (userType != null && userType.equals("Lecturer")) {
            setContentView(R.layout.activity_homeforlecturer);
        } else if (userType != null && userType.equals("Student")) {
            setContentView(R.layout.activity_home);
        } else if (userType != null && userType.equals("Admin")){
            setContentView(R.layout.activity_homeforadmin);
        } else {
            setContentView(R.layout.activity_home);
        }

        try (DatabaseHelper db = new DatabaseHelper(this)) {
            userUUID = getIntent().getStringExtra("USER_UUID");
            TextView tvWelcome = findViewById(R.id.tvWelcome);
            String nameFromDb = db.getUserNameFromUUID(userUUID);
            tvWelcome.setText(getString(R.string.welcome, nameFromDb));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        if (departmentId == 0) {
//TODO              departmentId burada popup ile seçilecek. RegisterActivity deki değer 0 olarak ayarlandı.
//        }
    }

    public void onBtnQRCodeClick(View view) {
        Intent intent;
        if (userType != null && userType.equals("Lecturer")) {
            intent = new Intent(HomeActivity.this, QRCodeCreatorActivity.class);
        } else if (userType != null && userType.equals("Student")) {
            intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
        } else if (userType != null && userType.equals("Admin")){
            intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
        } else {
            intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
        }
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnNFCClick(View view) {
        Toast.makeText(this, "NFC Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBtnClassCodeClick(View view) {
        Intent intent;
        if (userType != null && userType.equals("Lecturer")) {
            intent = new Intent(HomeActivity.this, QRCodeCreatorActivity.class);
        } else if (userType != null && userType.equals("Student")) {
            intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
        } else if (userType != null && userType.equals("Admin")){
            intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
        } else {
            intent = new Intent(HomeActivity.this, QRCodeScannerActivity.class);
        }
        intent.putExtra("USER_UUID", userUUID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBtnAttendHistoryClick(View view) {
        Toast.makeText(this, "Attend History Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onBtnProfileClick(View view) {
        Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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
