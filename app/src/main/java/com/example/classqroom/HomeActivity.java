package com.example.classqroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onBtnQRCodeClick(View view) {
        //TODO burası otomatik açılan bir pencere olacak. Kamera açılıp okutulduktan sonra katılınınan ders(popup) görünecek.
    }

    public void onBtnNFCClick(View view) {
        //TODO
    }

    public void onBtnClassCodeClick(View view) {
        //TODO
    }

    public void onBtnAttendHistoryClick(View view) {
        //TODO
    }

    public void onBtnProfileClick(View view) {
        //TODO
    }

    public void onBtnSettingsClick(View view) {
        //TODO
    }

    public void onBtnSupportClick(View view) {
        //TODO
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        HomeActivity.this.finish();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
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
