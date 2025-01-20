package com.mrtkyr.classqroom.student;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.content.Context;

public class QRCodeScannerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String userUUID = getIntent().getStringExtra("USER_UUID");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodescanner);

        try (DatabaseHelper db = new DatabaseHelper(this)) {
            TextView tvWelcome = findViewById(R.id.tvWelcome);
            String nameFromDb = db.getUserNameFromUUID(userUUID);
            tvWelcome.setText(getString(R.string.welcome, nameFromDb));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(v -> startScan());

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBtnBackClick());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void onBtnBackClick() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void startScan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.qrcodescannermsg));
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setTimeout(10000);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);
    }

    private void vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            Vibrator vibrator = vibratorManager.getDefaultVibrator();
            vibrator.vibrate(VibrationEffect.createWaveform(
                    new long[]{0, 100, 50, 100},
                    -1
            ));
        }
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    vibrate();
                    Toast.makeText(this, "QR code: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });
}
