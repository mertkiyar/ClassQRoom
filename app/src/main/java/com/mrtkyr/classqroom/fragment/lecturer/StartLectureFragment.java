package com.mrtkyr.classqroom.fragment.lecturer;

import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.SessionManager;
import com.mrtkyr.classqroom.api.AttendanceApi;
import com.mrtkyr.classqroom.api.LecturerApi;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.enums.AttendanceType;
import com.mrtkyr.classqroom.model.AttendanceModel;
import com.mrtkyr.classqroom.model.AttendanceSessionModel;
import com.mrtkyr.classqroom.model.CourseModel;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartLectureFragment extends Fragment {
    private static final long QR_POLL_INTERVAL_MS = 3000L;

    private UUID lecturerUUID;
    private AutoCompleteTextView courseAutoComplete, attendanceTypeAutoComplete;
    private ImageView ivQRCode;
    private LinearLayout llCodeBox;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private CourseModel selectedCourse;
    private final List<String> attendanceTypesList = new ArrayList<>();
    private NfcAdapter nfcAdapter;
    private Runnable qrPollingRunnable;
    private UUID lastSessionId;

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

        SessionManager sessionManager = new SessionManager(getContext());
        if (sessionManager.getToken() == null || sessionManager.getToken().isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.MSG_USER_UID_NOT_FOUND), Toast.LENGTH_SHORT).show();
            return;
        }

        UserApi userApi = ApiClient.getClient(getContext()).create(UserApi.class);
        userApi.me().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<UserModel>> call,
                                   @NonNull Response<RootResponse<UserModel>> response) {
                if (response.body() != null) {
                    lecturerUUID = response.body().getData().getUserId();

                    courseAutoComplete = view.findViewById(R.id.lectureAutoCompleteTextView);
                    attendanceTypeAutoComplete = view.findViewById(R.id.attendanceTypeAutoCompleteTextView);
                    ivQRCode = view.findViewById(R.id.ivQRCode);
                    llCodeBox = view.findViewById(R.id.llCodeBox);
                    Button btnStartLecture = view.findViewById(R.id.btnStartLecture);
                    btnStartLecture.setEnabled(false);

                    LecturerApi lecturerApi = ApiClient.getClient(getContext()).create(LecturerApi.class);
                    lecturerApi.getCoursesByLecturer(lecturerUUID)
                            .enqueue(new Callback<>() {
                                @Override
                                public void onResponse(@NonNull Call<RootResponse<List<CourseModel>>> call,
                                                       @NonNull Response<RootResponse<List<CourseModel>>> response) {
                                    if (response.body() != null && !response.body().getData().isEmpty()) {
                                        List<CourseModel> coursesList = new ArrayList<>(response.body().getData());

                                        if (getContext() != null && !coursesList.isEmpty()) {
                                            ArrayAdapter<CourseModel> adapter = new ArrayAdapter<>(
                                                    getContext(),
                                                    android.R.layout.simple_dropdown_item_1line,
                                                    coursesList);
                                            courseAutoComplete.setAdapter(adapter);
                                        }

                                        if (attendanceTypesList.isEmpty()) {
                                            attendanceTypesList.add(getString(R.string.TEXT_QR_CODE));
                                            attendanceTypesList.add(getString(R.string.TEXT_NFC));
                                            attendanceTypesList.add(getString(R.string.TEXT_SIX_DIGIT_CODE));
                                        }

                                        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                                                requireContext(),
                                                android.R.layout.simple_dropdown_item_1line,
                                                attendanceTypesList);

                                        attendanceTypeAutoComplete.setAdapter(typeAdapter);

                                        if (!attendanceTypesList.isEmpty()) {
                                            attendanceTypeAutoComplete.setText(attendanceTypesList.get(0), false);
                                        }

                                        courseAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
                                            selectedCourse = (CourseModel) parent.getItemAtPosition(position);
                                            btnStartLecture.setEnabled(true);
                                        });
                                        btnStartLecture.setOnClickListener(v -> startLecture());
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<RootResponse<List<CourseModel>>> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_COURSES), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<UserModel>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_USERS), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopQRCodeUpdates();
    }

    public void startLecture() {
        if (selectedCourse == null) {
            Toast.makeText(getContext(), getString(R.string.TEXT_SELECT_COURSE), Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedType = attendanceTypeAutoComplete.getText().toString();

        AttendanceModel attendanceModel = new AttendanceModel();
        attendanceModel.setCourse(selectedCourse);
        attendanceModel.setLatitude(null);
        attendanceModel.setLongitude(null);
        attendanceModel.setAllowedRadiusMeters(null);

        if (selectedType.equals(getString(R.string.TEXT_QR_CODE))) {
            attendanceModel.setAttendanceType(AttendanceType.QR_CODE);
        } else if (selectedType.equals(getString(R.string.TEXT_NFC))) {
            attendanceModel.setAttendanceType(AttendanceType.NFC);
        } else if (selectedType.equals(getString(R.string.TEXT_SIX_DIGIT_CODE))) {
            attendanceModel.setAttendanceType(AttendanceType.SIX_DIGIT_CODE);
        }

        attendanceModel.setSessionHours(Short.valueOf("1"));
        attendanceModel.setStartedAt(LocalDateTime.now());
        attendanceModel.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        attendanceModel.setActive(true);

        AttendanceApi attendanceApi = ApiClient.getClient(getContext()).create(AttendanceApi.class);
        attendanceApi.startAttendance(attendanceModel).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<AttendanceModel>> call,
                                   @NonNull Response<RootResponse<AttendanceModel>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    UUID attendanceId = response.body().getData().getAttendanceId();

                    if (selectedType.equals(getString(R.string.TEXT_QR_CODE))) {
                        startQRCodeUpdates(attendanceId);
                    } else if (selectedType.equals(getString(R.string.TEXT_NFC))) {
                        if (nfcAdapter == null) {
                            Toast.makeText(requireContext(), getString(R.string.MSG_NFC_NOT_SUPPORTED), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (!nfcAdapter.isEnabled()) {
                            Toast.makeText(requireContext(), getString(R.string.MSG_NFC_NOT_ENABLED), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UUID nfcPath = response.body().getData().getNfcPath();
                        if (nfcPath != null) {
                            NFCWriterFragment nfcWriterFragment = NFCWriterFragment.newInstance(nfcPath.toString());
                            nfcWriterFragment.show(requireActivity().getSupportFragmentManager(), "nfc_writer");
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.ERROR_SESSION_CREATE), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.ERROR_SESSION_CREATE), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<AttendanceModel>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_COURSE), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startQRCodeUpdates(UUID attendanceId) {
        lastSessionId = null;
        llCodeBox.setVisibility(View.INVISIBLE);
        ivQRCode.setVisibility(View.VISIBLE);

        AttendanceApi attendanceApi = ApiClient.getClient(getContext()).create(AttendanceApi.class);

        qrPollingRunnable = new Runnable() {
            @Override
            public void run() {
                if (getContext() == null) return;

                attendanceApi.getCurrentSession(attendanceId.toString()).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<RootResponse<AttendanceSessionModel>> call,
                                           @NonNull Response<RootResponse<AttendanceSessionModel>> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            AttendanceSessionModel session = response.body().getData();
                            UUID sessionId = session.getAttendanceSessionId();

                            if (!sessionId.equals(lastSessionId)) {
                                lastSessionId = sessionId;
                                String qrPayload = attendanceId + "_" + sessionId;
                                Bitmap qrBitmap = generateQRCode(qrPayload);
                                if (qrBitmap != null && ivQRCode != null) {
                                    ivQRCode.setImageBitmap(qrBitmap);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RootResponse<AttendanceSessionModel>> call, @NonNull Throwable t) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_SESSION), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                handler.postDelayed(this, QR_POLL_INTERVAL_MS);
            }
        };

        handler.post(qrPollingRunnable);
    }

    private void stopQRCodeUpdates() {
        if (qrPollingRunnable != null) {
            handler.removeCallbacks(qrPollingRunnable);
            qrPollingRunnable = null;
        }
    }

    private Bitmap generateQRCode(String token) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(token, BarcodeFormat.QR_CODE, 500, 500);
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
    }
}
