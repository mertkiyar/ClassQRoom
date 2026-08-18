package com.mrtkyr.classqroom.fragment.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.api.AttendanceApi;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.model.AttendanceRecordModel;
import com.mrtkyr.classqroom.model.AttendanceSessionModel;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;
import com.mrtkyr.classqroom.enums.AttendanceType;
import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeFragment extends Fragment {
    private EditText[] codeBoxes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnEnterCode = view.findViewById(R.id.btnEnterCode);
        codeBoxes = new EditText[]{
                view.findViewById(R.id.codeBox1),
                view.findViewById(R.id.codeBox2),
                view.findViewById(R.id.codeBox3),
                view.findViewById(R.id.codeBox4),
                view.findViewById(R.id.codeBox5),
                view.findViewById(R.id.codeBox6)
        };

        for (int i = 0; i < codeBoxes.length; i++) {
            final int currentIndex = i;

            codeBoxes[currentIndex].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && currentIndex < codeBoxes.length - 1) {
                        codeBoxes[currentIndex + 1].requestFocus();
                    }
                }
            });
            codeBoxes[currentIndex].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (codeBoxes[currentIndex].getText().toString().isEmpty() && currentIndex > 0) {
                        codeBoxes[currentIndex - 1].requestFocus();
                        codeBoxes[currentIndex - 1].setText("");
                    }
                }
                return false;
            });
        }
        btnEnterCode.setOnClickListener(v -> enterCode());
    }

    private void enterCode() {
        String enteredCode = getEnteredCode();
        if (enteredCode.length() != 6) {
            Toast.makeText(getContext(), getString(R.string.MSG_CODE_INVALID), Toast.LENGTH_LONG).show();
            return;
        }

        AttendanceApi attendanceApi = ApiClient.getClient(getContext()).create(AttendanceApi.class);
        attendanceApi.getAttendanceSessionByCode(enteredCode).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<AttendanceSessionModel>> call,
                                   @NonNull Response<RootResponse<AttendanceSessionModel>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    AttendanceSessionModel session = response.body().getData();

                    UserApi userApi = ApiClient.getClient(getContext()).create(UserApi.class);
                    userApi.me().enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<RootResponse<UserModel>> userCall,
                                               @NonNull Response<RootResponse<UserModel>> userResponse) {
                            if (userResponse.body() != null && userResponse.body().getData() != null) {
                                String userIp = "127.0.0.1"; // Default or dynamic if implemented later

                                AttendanceRecordModel record = new AttendanceRecordModel();
                                record.setStudentId(userResponse.body().getData().getUserId());
                                record.setAttendanceSessionId(session.getAttendanceSessionId());
                                record.setAttendanceType(AttendanceType.SIX_DIGIT_CODE);
                                record.setCurrentLat(null);
                                record.setCurrentLong(null);
                                record.setAttendAt(LocalDateTime.now());
                                record.setLate(false);
                                record.setDeviceId(null); //todo will be implemented later
                                record.setClientIp(userIp);

                                attendanceApi.takeAttendance(record).enqueue(new Callback<>() {
                                    @Override
                                    public void onResponse(@NonNull Call<RootResponse<Void>> takeCall,
                                                           @NonNull Response<RootResponse<Void>> takeResponse) {
                                        if (takeResponse.isSuccessful()) {
                                            if (getContext() != null) {
                                                Toast.makeText(getContext(), getString(R.string.MSG_ATTENDANCE_SUCCESS), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            if (getContext() != null) {
                                                Toast.makeText(getContext(), getString(R.string.MSG_ALREADY_ATTENDED), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<RootResponse<Void>> takeCall, @NonNull Throwable t) {
                                        if (getContext() != null) {
                                            Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_COURSE), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<RootResponse<UserModel>> userCall, @NonNull Throwable t) {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_COURSE), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), getString(R.string.MSG_CODE_INVALID), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<AttendanceSessionModel>> call, @NonNull Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), getString(R.string.MSG_CODE_INVALID), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getEnteredCode() {
        StringBuilder code = new StringBuilder();
        for (EditText box : codeBoxes) {
            code.append(box.getText().toString());
        }
        return code.toString();
    }
}