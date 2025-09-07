package com.mrtkyr.classqroom.fragment.student;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtkyr.classqroom.R;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class NFCScannerFragment extends DialogFragment {
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private TextView tvNFCStatus;
    private Button btnBackNFCScanner;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private boolean isNfcScanEnabled = false;

    FirebaseFirestore db;

    public static NFCScannerFragment newInstance(String userUID) {
        NFCScannerFragment fragment = new NFCScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireActivity());
        setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams params = window.getAttributes();
                params.dimAmount = 0.60f;
                params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(params);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_nfc, container, false);
        tvNFCStatus = view.findViewById(R.id.tvNFCStatus);
        btnBackNFCScanner = view.findViewById(R.id.btnBackNFCScanner);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            this.mUserUID = getArguments().getString(ARG_USER_UID);
        }

        db = FirebaseFirestore.getInstance();
        prepareNfcForegroundDispatch();

        isNfcScanEnabled = true;
        enableNfcDispatch();
        tvNFCStatus.setText(R.string.TEXT_NFC_STATUS_SCANNING);

        btnBackNFCScanner.setOnClickListener(v -> {
            isNfcScanEnabled = false;
            disableNfcDispatch();
            dismiss();
        });
    }

    private void prepareNfcForegroundDispatch() {
        Intent intent = new Intent(requireActivity(), requireActivity().getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("MIME type", e);
        }
        intentFilters = new IntentFilter[]{ndef};
    }

    private void enableNfcDispatch() {
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(requireActivity(), pendingIntent, intentFilters, null);
        }
    }

    private void disableNfcDispatch() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(requireActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNfcScanEnabled) {
            enableNfcDispatch();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disableNfcDispatch();
    }

    public void handleNfcIntent(Intent intent) {
        if (!isNfcScanEnabled) {
            return;
        }

        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
            if (messages.length > 0) {
                NdefRecord record = messages[0].getRecords()[0];
                byte[] payload = record.getPayload();
                String token = new String(payload, StandardCharsets.UTF_8).substring(3);
                sendAttendanceToDatabase(token);
                isNfcScanEnabled = false;
            }
        }
    }

    private void sendAttendanceToDatabase(String token) {
        if (!token.contains("_")) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), getString(R.string.MSG_NFC_INVALID), Toast.LENGTH_SHORT).show();
            }
            return;
        }
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
                                            attendance.put("type", "NFC");

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
                                                            dismiss();
                                                        }
                                                    });
                                        } else {
                                            if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        }
                                    }).addOnFailureListener(lectureError -> {
                                        if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    });
                                } else {
                                    if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }).addOnFailureListener(userError -> {
                                if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                dismiss();
                            });
                        } else {
                            Toast.makeText(getContext(), getString(R.string.MSG_QR_CODE_INVALID), Toast.LENGTH_LONG).show();
                            dismiss();
                        }
                    });
        } else {
            Toast.makeText(getContext(), getString(R.string.MSG_QR_CODE_INVALID), Toast.LENGTH_LONG).show();
            dismiss();
        }
    }
}