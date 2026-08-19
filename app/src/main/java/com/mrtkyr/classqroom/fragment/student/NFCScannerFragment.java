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

import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.SessionManager;
import com.mrtkyr.classqroom.api.AttendanceApi;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.enums.AttendanceType;
import com.mrtkyr.classqroom.model.AttendanceRecordModel;
import com.mrtkyr.classqroom.model.AttendanceSessionModel;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCScannerFragment extends DialogFragment {
    private TextView tvNFCStatus;
    private Button btnBackNFCScanner;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private boolean isNfcScanEnabled = false;

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

        SessionManager sessionManager = new SessionManager(getContext());
        if (sessionManager.getToken() == null || sessionManager.getToken().isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.MSG_USER_UID_NOT_FOUND), Toast.LENGTH_SHORT).show();
            return;
        }

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

        Parcelable[] rawMessages;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, Parcelable.class);
        } else {
            //noinspection deprecation
            rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        }
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
        if (token == null || token.isEmpty()) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), getString(R.string.MSG_NFC_INVALID), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        AttendanceApi attendanceApi = ApiClient.getClient(getContext()).create(AttendanceApi.class);
        attendanceApi.getAttendanceSessionByNfcPath(token).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<AttendanceSessionModel>> call,
                                   @NonNull Response<RootResponse<AttendanceSessionModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    AttendanceSessionModel session = response.body().getData();

                    UserApi userApi = ApiClient.getClient(getContext()).create(UserApi.class);
                    userApi.me().enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<RootResponse<UserModel>> call,
                                               @NonNull Response<RootResponse<UserModel>> response) {
                            if (response.body() == null || response.body().getData() == null) return;

                            AttendanceRecordModel record = new AttendanceRecordModel();
                            record.setStudentId(response.body().getData().getUserId());
                            record.setAttendanceSessionId(session.getAttendanceSessionId());
                            record.setAttendanceType(AttendanceType.NFC);
                            record.setCurrentLat(null); //todo to be implemented location and ip fields
                            record.setCurrentLong(null);
                            record.setAttendAt(LocalDateTime.now());
                            record.setLate(false); //todo update with this condition: currentTime - startedTime > 15mins is true
                            record.setDeviceId(null);
                            record.setClientIp("0.0.0.0");

                            attendanceApi.takeAttendance(record).enqueue(new Callback<>() {
                                @Override
                                public void onResponse(@NonNull Call<RootResponse<Void>> call,
                                                       @NonNull Response<RootResponse<Void>> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getContext(), getString(R.string.MSG_ATTENDANCE_SUCCESS), Toast.LENGTH_LONG).show();
                                        dismiss();
                                    } else {
                                        String errorMsg = getString(R.string.MSG_ALREADY_ATTENDED);
                                        try {
                                            try (okhttp3.ResponseBody errorBody = response.errorBody()) {
                                                if (errorBody != null) {
                                                    String errorJson = errorBody.string();
                                                    JSONObject jsonObject = new JSONObject(errorJson);
                                                    if (jsonObject.has("exception")) {
                                                        JSONObject exceptionObj = jsonObject.getJSONObject("exception");
                                                        if (exceptionObj.has("message")) {
                                                            errorMsg = exceptionObj.getString("message");
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            android.util.Log.e("NFCScannerFragment", "Error parsing error response", e);
                                        }
                                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                                        dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<RootResponse<Void>> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_COURSE), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            });
                        }

                        @Override
                        public void onFailure(@NonNull Call<RootResponse<UserModel>> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_USERS), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), getString(R.string.MSG_NFC_INVALID), Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<AttendanceSessionModel>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_COURSE), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}