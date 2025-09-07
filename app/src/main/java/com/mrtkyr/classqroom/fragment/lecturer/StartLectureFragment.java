package com.mrtkyr.classqroom.fragment.lecturer;

import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mrtkyr.classqroom.R;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class StartLectureFragment extends Fragment {
    FirebaseFirestore db;
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private AutoCompleteTextView lectureAutoComplete, attendanceTypeAutoComplete;
    private ImageView ivQRCode;
    private LinearLayout llCodeBox;
    private EditText codeBox1, codeBox2, codeBox3, codeBox4, codeBox5, codeBox6;
    private final Handler handler = new Handler();
    private static final SecureRandom RAND = new SecureRandom();
    private final List<String> attendanceTypesList = new ArrayList<>();
    private NfcAdapter nfcAdapter;

    public static StartLectureFragment newInstance(String userUID) {
        StartLectureFragment fragment = new StartLectureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_lecture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        codeBox1 = view.findViewById(R.id.codeBox1);
        codeBox2 = view.findViewById(R.id.codeBox2);
        codeBox3 = view.findViewById(R.id.codeBox3);
        codeBox4 = view.findViewById(R.id.codeBox4);
        codeBox5 = view.findViewById(R.id.codeBox5);
        codeBox6 = view.findViewById(R.id.codeBox6);

        lectureAutoComplete = view.findViewById(R.id.lectureAutoCompleteTextView);
        attendanceTypeAutoComplete = view.findViewById(R.id.attendanceTypeAutoCompleteTextView);
        ivQRCode = view.findViewById(R.id.ivQRCode);
        llCodeBox = view.findViewById(R.id.llCodeBox);
        Button btnStartLecture = view.findViewById(R.id.btnStartLecture);
        btnStartLecture.setEnabled(false);

        if (getArguments() != null) {
            mUserUID = getArguments().getString(ARG_USER_UID);
        }

        if (mUserUID == null || mUserUID.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.MSG_USER_UID_NOT_FOUND), Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("lectures")
                .whereEqualTo("lecturerUID", mUserUID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> lectureNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String lectureName = document.getString("name");
                            if (lectureName != null) {
                                lectureNames.add(lectureName);
                            }
                        }

                        if (getContext() != null && !lectureNames.isEmpty()) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    lectureNames
                            );
                            lectureAutoComplete.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_LECTURES), Toast.LENGTH_SHORT).show();
                    }
                });

        if (attendanceTypesList.isEmpty()) {
            attendanceTypesList.add(getString(R.string.TEXT_QR_CODE));
            attendanceTypesList.add(getString(R.string.TEXT_NFC));
            attendanceTypesList.add(getString(R.string.TEXT_SIX_DIGIT_CODE));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                attendanceTypesList
        );

        attendanceTypeAutoComplete.setAdapter(adapter);

        if (!attendanceTypesList.isEmpty()) {
            attendanceTypeAutoComplete.setText(attendanceTypesList.get(0), false);
        }

        lectureAutoComplete.setOnItemClickListener((parent, view1, position, id) -> btnStartLecture.setEnabled(true));
        btnStartLecture.setOnClickListener(v -> startLecture());
    }

    public void startLecture() {
        String lectureName = lectureAutoComplete.getText().toString();
        String lectureType = attendanceTypeAutoComplete.getText().toString();

        db.collection("lectures")
                .whereEqualTo("name", lectureName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {

                        if (lectureType.equals(getString(R.string.TEXT_QR_CODE))) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String lectureUID = document.getId();
                                startQRCodeUpdates(lectureUID);
                            }
                        }

                        else if (lectureType.equals(getString(R.string.TEXT_NFC))) {
                            if (nfcAdapter == null) {
                                Toast.makeText(requireContext(), getString(R.string.MSG_NFC_NOT_SUPPORTED), Toast.LENGTH_LONG).show();
                                return;
                            }

                            if (!nfcAdapter.isEnabled()) {
                                Toast.makeText(requireContext(), getString(R.string.MSG_NFC_NOT_ENABLED), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String lectureUID = task.getResult().getDocuments().get(0).getId();
                            NFCWriterFragment dialog = NFCWriterFragment.newInstance(mUserUID, lectureUID);
                            dialog.show(getParentFragmentManager(), "nfc_writer");
                        }

                        else if (lectureType.equals(getString(R.string.TEXT_SIX_DIGIT_CODE))) {
                            String lectureUID = task.getResult().getDocuments().get(0).getId();

                            db.collection("lectures")
                                    .document(lectureUID)
                                    .collection("sessions")
                                    .add(sessionCreator("6-Digit Code"))
                                    .addOnSuccessListener(sessionRef -> {
                                        String sessionUID = sessionRef.getId();
                                        String sixDigitCodeStr = String.valueOf(sixDigitCode());

                                        HashMap<String, Object> codes = new HashMap<>();
                                        codes.put("lectureUID", lectureUID);
                                        codes.put("sessionUID", sessionUID);
                                        db.collection("codes")
                                                .document(sixDigitCodeStr)
                                                .set(codes)
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    sessionCreator("6-Digit Code");
                                                    ivQRCode.setVisibility(View.INVISIBLE);
                                                    llCodeBox.setVisibility(View.VISIBLE);
                                                    showGeneratedCode(sixDigitCodeStr);
                                                })
                                                .addOnFailureListener(Throwable::getMessage);

                                    });
                        }
                    }
                });
    }

    public void startQRCodeUpdates(String lectureUID) {
        Runnable updateQRCodeRunnable =  new Runnable() {
            String currentSessionUID;
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if (currentSessionUID != null && !currentSessionUID.isEmpty()) {
                    db.collection("lectures")
                            .document(lectureUID)
                            .collection("sessions")
                            .document(currentSessionUID)
                            .update("isActive", false)
                            .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), getString(R.string.MSG_SESSION_DISABLED), Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(requireContext(), getString(R.string.ERROR_SESSION_DISABLED), Toast.LENGTH_SHORT).show());
                }

                db.collection("lectures")
                        .document(lectureUID)
                        .collection("sessions")
                        .add(sessionCreator("QR Code"))
                        .addOnSuccessListener(sessionRef -> {
                            currentSessionUID = sessionRef.getId();

                            String token = (lectureUID + "_" + currentSessionUID);
                            Bitmap qrCodeBitmap = generateQRCode(token);

                            if (qrCodeBitmap != null) {
                                llCodeBox.setVisibility(View.INVISIBLE);
                                ivQRCode.setVisibility(View.VISIBLE);
                                ivQRCode.setImageBitmap(qrCodeBitmap);
                            }
                        });

                handler.postDelayed(this, 600000); //TODO will move server-side(as 15 second)
            }
        };
        handler.post(updateQRCodeRunnable);
    }

    private HashMap<String, Object> sessionCreator(String type) {
        HashMap<String, Object> session = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, 50);
        Date fiftyMinutesLater = cal.getTime();

        session.put("createdAt", new Timestamp(now));
        session.put("expiresAt", new Timestamp(fiftyMinutesLater));
        session.put("isActive", true);
        session.put("type", type);
        return session;
    }

    private Bitmap generateQRCode(String token) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(token, BarcodeFormat.QR_CODE, 500, 500);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static int sixDigitCode() {
        return 100_000 + RAND.nextInt(900_000);
    }

    private void showGeneratedCode(String code) {
        if (code.length() != 6) return;
        codeBox1.setText(String.valueOf(code.charAt(0)));
        codeBox2.setText(String.valueOf(code.charAt(1)));
        codeBox3.setText(String.valueOf(code.charAt(2)));
        codeBox4.setText(String.valueOf(code.charAt(3)));
        codeBox5.setText(String.valueOf(code.charAt(4)));
        codeBox6.setText(String.valueOf(code.charAt(5)));
    }
}