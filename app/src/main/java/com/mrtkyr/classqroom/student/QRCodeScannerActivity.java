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

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class QRCodeScannerActivity extends AppCompatActivity {

    private static final String SECRET_KEY = "QXpK[M9A{^iMI-[BT825fbK0DgG-9-uR";
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
                    String jwtToken = result.getContents();
                    Claims claims = parseJWT(jwtToken);

                    if (claims != null) {
                        String lectureId = claims.get("lecture_id", String.class);
                        String lecturerId = claims.get("lecturer_id", String.class);

                        Toast.makeText(this, "Ders ID: " + lectureId + "\nÖğretmen ID: " + lecturerId, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Geçersiz QR Kodu!", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private SecretKey getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
    }

    private Claims parseJWT(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt);
            return jws.getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
