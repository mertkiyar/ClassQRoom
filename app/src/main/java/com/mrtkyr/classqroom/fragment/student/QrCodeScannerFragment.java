package com.mrtkyr.classqroom.fragment.student;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mrtkyr.classqroom.R;

import java.util.HashMap;

public class QrCodeScannerFragment extends Fragment {
    private FirebaseFirestore db;
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;

    public static QrCodeScannerFragment newInstance(String userUID) {
        QrCodeScannerFragment fragment = new QrCodeScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_code_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            this.mUserUID = getArguments().getString(ARG_USER_UID);
        }

        db = FirebaseFirestore.getInstance();
        Button btnScanQRCode = view.findViewById(R.id.btnScanQRCode);

        btnScanQRCode.setOnClickListener(v -> startScan());
    }

    public void startScan() {
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.MSG_QR_CODE_SCAN));
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setTimeout(10000);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);
    }

    private void vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) requireContext()
                    .getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            if (vibratorManager != null) {
                Vibrator vibrator = vibratorManager.getDefaultVibrator();
                vibrator.vibrate(VibrationEffect.createWaveform(
                        new long[]{0, 100, 50, 100},
                        -1
                ));
            }
        }
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    vibrate();
                    String token = result.getContents();
                    if (token.contains("_")) {
                        String lectureUID = token.substring(0, token.indexOf("_"));
                        String sessionUID = token.substring(token.indexOf("_") + 1);
                        if (!lectureUID.isEmpty() && !sessionUID.isEmpty()) {
                            db.collection("lectures")
                                    .document(lectureUID)
                                    .collection("sessions")
                                    .document(sessionUID)
                                    .get()
                                    .addOnSuccessListener(task -> {
                                        if (Boolean.TRUE.equals(task.get("isActive"))) {
                                            String attendanceUID =  lectureUID + "_" + sessionUID + "_" + mUserUID;
                                            DocumentReference userRef = db.collection("users").document(mUserUID);
                                            DocumentReference lectureRef = db.collection("lectures").document(lectureUID);

                                            userRef.get().addOnSuccessListener(userDocument -> {
                                                if (userDocument.exists()) {
                                                    String studentName = userDocument.getString("name") + " " + userDocument.getString("surname");
                                                    lectureRef.get().addOnSuccessListener(lectureDocument -> {
                                                        if (lectureDocument.exists()) {
                                                            String lectureName = lectureDocument.getString("name");
                                                            String lecturerUID = lectureDocument.getString("lecturerUID");

                                                            HashMap<String, Object> attendance = new HashMap<>();
                                                            attendance.put("studentName", studentName);
                                                            attendance.put("lectureName", lectureName);
                                                            attendance.put("lecturerUID", lecturerUID);
                                                            attendance.put("lectureUID", lectureUID);
                                                            attendance.put("sessionUID", sessionUID);
                                                            attendance.put("studentUID", mUserUID);
                                                            attendance.put("scannedAt", Timestamp.now());
                                                            attendance.put("status", "Present");
                                                            attendance.put("type", "QR Code");

                                                            db.collection("attendances")
                                                                    .document(attendanceUID)
                                                                    .set(attendance)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        if (getContext() != null) {
                                                                            Toast.makeText(getContext(), getString(R.string.MSG_ATTENDANCE_SUCCESS), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        if (getContext() != null) {
                                                                            Toast.makeText(getContext(), getString(R.string.MSG_ALREADY_ATTENDED), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });
                                                        } else {
                                                            if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(lectureError -> {
                                                        if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                    });
                                                } else {
                                                    if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(userError -> {
                                                if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                            });
                                        } else {
                                            Toast.makeText(getContext(), getString(R.string.MSG_QR_CODE_INVALID), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), getString(R.string.MSG_QR_CODE_INVALID), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.MSG_QR_CODE_INVALID), Toast.LENGTH_LONG).show();
                    }
                }
            });

}