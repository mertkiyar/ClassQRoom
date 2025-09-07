package com.mrtkyr.classqroom.fragment.lecturer;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtkyr.classqroom.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NFCWriterFragment extends DialogFragment {
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private TextView tvNFCStatus;
    private Button btnBackNFCScanner;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private boolean isNfcScanEnabled = false;
    private static final String ARG_LECTURE_UID = "lectureUID";
    private String mLectureUID;

    FirebaseFirestore db;

    public static NFCWriterFragment newInstance(String userUID, String lectureUID) {
        NFCWriterFragment fragment = new NFCWriterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        args.putString(ARG_LECTURE_UID, lectureUID);
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
            this.mLectureUID = getArguments().getString(ARG_LECTURE_UID);
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

    public void onNfcTagReceived(Tag tag) {
        db.collection("lectures")
                .document(mLectureUID)
                .collection("sessions")
                .add(sessionCreator())
                .addOnSuccessListener(sessionRef -> {
                    String sessionUID = sessionRef.getId();
                    String dataToWrite = mLectureUID + "_" + sessionUID;

                    try {
                        writeToNfcTag(tag, dataToWrite);
                        Toast.makeText(getContext(), getString(R.string.MSG_WRITE_SUCCESS), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } catch (IOException | FormatException e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), getString(R.string.ERROR_WRITE_NFC), Toast.LENGTH_LONG).show();
                        }
                        sessionRef.delete().addOnSuccessListener(aVoid -> Toast.makeText(getContext(), getString(R.string.MSG_SESSION_DELETE), Toast.LENGTH_SHORT).show()).addOnFailureListener(deleteError -> Toast.makeText(getContext(), getString(R.string.ERROR_SESSION_DELETE), Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), getString(R.string.ERROR_SESSION_CREATE), Toast.LENGTH_SHORT).show());
    }

    private void writeToNfcTag(Tag tag, String data) throws IOException, FormatException {
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{
                        NdefRecord.createTextRecord("en", data)
                });

        Ndef ndef = Ndef.get(tag);

        if (ndef != null) {
            ndef.connect();

            if (!ndef.isWritable()) {
                throw new IOException(getString(R.string.MSG_TAG_NOT_WRITABLE));
            }

            if (ndef.getMaxSize() < ndefMessage.toByteArray().length) {
                throw new IOException(getString(R.string.MSG_BIG_DATA_TO_WRITE_TAG));
            }

            ndef.writeNdefMessage(ndefMessage);
            ndef.close();
        } else {
            NdefFormatable format = NdefFormatable.get(tag);
            if (format != null) {
                format.connect();
                format.format(ndefMessage);
                format.close();
            } else {
                throw new IOException(getString(R.string.MSG_TAG_NOT_SUPPORT_NDEF));
            }
        }
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
    private HashMap<String, Object> sessionCreator() {
        HashMap<String, Object> session = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, 50);
        Date fiftyMinutesLater = cal.getTime();

        session.put("createdAt", new Timestamp(now));
        session.put("expiresAt", new Timestamp(fiftyMinutesLater));
        session.put("isActive", true);
        session.put("type", "NFC");
        return session;
    }
}