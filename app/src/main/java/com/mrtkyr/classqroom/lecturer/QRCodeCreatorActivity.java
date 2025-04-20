package com.mrtkyr.classqroom.lecturer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;

import java.security.Key;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;

public class QRCodeCreatorActivity extends AppCompatActivity {

    private static final String SECRET_KEY = "QXpK[M9A{^iMI-[BT825fbK0DgG-9-uR";
    private ImageView ivQRCode;
    private Spinner spinLecture;
    private String userUUID;
    private Button btnStartLecture;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodecreator);

        userUUID = getIntent().getStringExtra("USER_UUID");
        ivQRCode = findViewById(R.id.ivQRCode);
        spinLecture = findViewById(R.id.spinLecture);
        btnStartLecture = findViewById(R.id.btnStartLecture);
        Button btnStartLecture = findViewById(R.id.btnStartLecture);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        try (DatabaseHelper databaseHelper = new DatabaseHelper(this)) {
            List<String> lectures = databaseHelper.getLecturesOfLecturer(userUUID);
            lectures.add(0, getString(R.string.selectlecture));
            ArrayAdapter<String> lectureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lectures);
            lectureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinLecture.setAdapter(lectureAdapter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void onClickStartLecture (View view) {
        try (DatabaseHelper databaseHelper = new DatabaseHelper(this)) {
            String lectureCode = databaseHelper.getLectureCode(spinLecture.getSelectedItem().toString());

            if (!spinLecture.getSelectedItem().toString().equals(getString(R.string.selectlecture))) {
                startQRCodeUpdates(lectureCode);
                btnStartLecture.setEnabled(false);
            } else {
                Toast.makeText(this, R.string.selectlecture, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String generateJWT(String lectureId, String lecturerId) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (50 * 60 * 1000);

        byte[] keyBytes = SECRET_KEY.getBytes();
        Key key = new SecretKeySpec(keyBytes, "HmacSHA256");

        return Jwts.builder()
                .claim("lecture_id", lectureId)
                .claim("lecturer_id", lecturerId)
                .issuedAt(new Date(nowMillis))
                .expiration(new Date(expMillis))
                .signWith(key)
                .compact();
    }

    private Bitmap generateQRCode(String text) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.QR_CODE, 500, 500);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void startQRCodeUpdates(String lectureCode) {
        Runnable updateQRCodeRunnable = new Runnable() {
            @Override
            public void run() {
                String jwtToken = generateJWT(lectureCode, userUUID);
                Bitmap qrCodeBitmap = generateQRCode(jwtToken);

                if (qrCodeBitmap != null) {
                    ivQRCode.setImageBitmap(qrCodeBitmap);
                }
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(updateQRCodeRunnable);
    }
}