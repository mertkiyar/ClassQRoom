package com.mrtkyr.classqroom.fragment.student;

import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtkyr.classqroom.R;

public class NFCFragment extends Fragment {
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private FirebaseFirestore db;
    private NfcAdapter nfcAdapter;

    public static NFCFragment newInstance(String userUID) {
        NFCFragment fragment = new NFCFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nfc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            this.mUserUID = getArguments().getString(ARG_USER_UID);
        }

        TextView tvNFC = view.findViewById(R.id.tvNFC);
        Button btnScanNFC = view.findViewById(R.id.btnScanNFC);

        if (nfcAdapter == null) {
            Toast.makeText(requireContext(), getString(R.string.MSG_NFC_NOT_SUPPORTED), Toast.LENGTH_LONG).show();
            btnScanNFC.setEnabled(false);
            return;
        }

        btnScanNFC.setOnClickListener(v -> {
            if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
                Toast.makeText(requireContext(), getString(R.string.MSG_NFC_NOT_ENABLED), Toast.LENGTH_SHORT).show();
            } else {
                NFCScannerFragment dialog = NFCScannerFragment.newInstance(mUserUID);
                dialog.show(getParentFragmentManager(), "nfc_scanner");
            }
        });
    }
}