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

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.api.AttendanceApi;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.enums.AttendanceType;
import com.mrtkyr.classqroom.model.AttendanceRecordModel;
import com.mrtkyr.classqroom.model.AttendanceSessionModel;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeScannerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_code_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                        new long[] { 0, 100, 50, 100 },
                        -1));
            }
        }
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    vibrate();
                    String qrContent = result.getContents();
                    String sessionId = qrContent.contains("_")
                            ? qrContent.substring(qrContent.indexOf("_") + 1)
                            : qrContent;
                    AttendanceApi attendanceApi = ApiClient.getClient(getContext()).create(AttendanceApi.class);
                    attendanceApi.getAttendanceSession(sessionId)
                            .enqueue(new Callback<>() {
                                @Override
                                public void onResponse(@NonNull Call<RootResponse<AttendanceSessionModel>> call,
                                                       @NonNull Response<RootResponse<AttendanceSessionModel>> response) {
                                    if (response.isSuccessful() && response.body() != null
                                            && response.body().getData() != null) {
                                        AttendanceSessionModel session = response.body().getData();

                                        UserApi userApi = ApiClient.getClient(getContext()).create(UserApi.class);
                                        userApi.me().enqueue(new Callback<>() {
                                            @Override
                                            public void onResponse(@NonNull Call<RootResponse<UserModel>> call,
                                                                   @NonNull Response<RootResponse<UserModel>> response) {
                                                if (response.body() == null || response.body().getData() == null)
                                                    return;

                                                AttendanceRecordModel record = new AttendanceRecordModel();
                                                record.setStudentId(response.body().getData().getUserId());
                                                record.setAttendanceSessionId(session.getAttendanceSessionId());
                                                record.setAttendanceType(AttendanceType.QR_CODE);
                                                record.setCurrentLat(null);
                                                record.setCurrentLong(null);
                                                record.setAttendAt(LocalDateTime.now());
                                                record.setLate(false);

                                                // todo implement actual device ID and client IP fetching
                                                record.setDeviceId(java.util.UUID.randomUUID());
                                                record.setClientIp("0.0.0.0");

                                                attendanceApi.takeAttendance(record)
                                                        .enqueue(new Callback<>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<RootResponse<Void>> call,
                                                                                   @NonNull Response<RootResponse<Void>> response) {
                                                                if (response.isSuccessful()) {
                                                                    Toast.makeText(getContext(),
                                                                            getString(R.string.MSG_ATTENDANCE_SUCCESS),
                                                                            Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    String errorMsg = getString(
                                                                            R.string.MSG_ALREADY_ATTENDED);
                                                                    try {
                                                                        if (response.errorBody() != null) {
                                                                            String errorJson = response.errorBody()
                                                                                    .string();
                                                                            org.json.JSONObject jsonObject = new org.json.JSONObject(
                                                                                    errorJson);
                                                                            if (jsonObject.has("exception")) {
                                                                                org.json.JSONObject exceptionObj = jsonObject
                                                                                        .getJSONObject("exception");
                                                                                if (exceptionObj.has("message")) {
                                                                                    errorMsg = exceptionObj
                                                                                            .getString("message");
                                                                                }
                                                                            }
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    Toast.makeText(getContext(), errorMsg,
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(@NonNull Call<RootResponse<Void>> call,
                                                                                  @NonNull Throwable t) {
                                                                Toast.makeText(getContext(),
                                                                        getString(R.string.ERROR_ATTEND_COURSE),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<RootResponse<UserModel>> call, @NonNull Throwable t) {
                                                Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_USERS),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<RootResponse<AttendanceSessionModel>> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

}
