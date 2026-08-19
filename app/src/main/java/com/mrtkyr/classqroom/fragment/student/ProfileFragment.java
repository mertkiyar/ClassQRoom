package com.mrtkyr.classqroom.fragment.student;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.SessionManager;
import com.mrtkyr.classqroom.api.LecturerApi;
import com.mrtkyr.classqroom.api.StudentApi;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.main.LoginActivity;
import com.mrtkyr.classqroom.model.LecturerModel;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.StudentModel;
import com.mrtkyr.classqroom.model.UserModel;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView tvFullName;
    private TextView tvStudentNumber;
    private TextView tvDepartment;
    private TextView tvGrade;
    private ImageView ivProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFullName = view.findViewById(R.id.tvFullName);
        tvStudentNumber = view.findViewById(R.id.tvStudentNumber);
        tvDepartment = view.findViewById(R.id.tvDepartment);
        tvGrade = view.findViewById(R.id.tvGrade);
        ivProfile = view.findViewById(R.id.ivProfile);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            if (getActivity() == null) return;
            new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.BUTTON_CONFIRM))
                    .setMessage(getString(R.string.MSG_LOGOUT))
                    .setPositiveButton(getString(R.string.BUTTON_YES), (dialog, which) -> {
                        SessionManager sessionManager = new SessionManager(getContext());
                        sessionManager.removeToken();
                        
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Bundle options = ActivityOptions.makeCustomAnimation(
                                getActivity(),
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        ).toBundle();
                        startActivity(intent, options);
                    })
                    .setNegativeButton(getString(R.string.BUTTON_NO), null)
                    .show();
        });
        getData();
    }

    private void getData() {
        if (getContext() == null) return;
        
        UserApi userApi = ApiClient.getClient(getContext()).create(UserApi.class);
        userApi.me().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<UserModel>> call,
                                   @NonNull Response<RootResponse<UserModel>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    UserModel user = response.body().getData();
                    String fullName = user.getFirstName() + " " + user.getLastName();
                    tvFullName.setText(fullName);

                    String userType = user.getUserType().toLowerCase();
                    UUID userId = user.getUserId();

                    if (userType.equals("student")) {
                        if ("male".equalsIgnoreCase(user.getGender()) || "MALE".equalsIgnoreCase(user.getGender())) {
                            ivProfile.setImageResource(R.drawable.img_male_student);
                        } else {
                            ivProfile.setImageResource(R.drawable.img_female_student);
                        }
                        fetchStudentProfile(userId);
                    } else if (userType.equals("lecturer") || userType.equals("admin")) {
                        if ("male".equalsIgnoreCase(user.getGender()) || "MALE".equalsIgnoreCase(user.getGender())) {
                            ivProfile.setImageResource(R.drawable.img_male_lecturer);
                        } else {
                            ivProfile.setImageResource(R.drawable.img_female_lecturer);
                        }
                        fetchLecturerProfile(userId);
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.ERROR_NOT_TAKEN_USER_INFO), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<UserModel>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.ERROR_NOT_TAKEN_USER_INFO) + ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchStudentProfile(UUID userId) {
        StudentApi studentApi = ApiClient.getClient(getContext()).create(StudentApi.class);
        studentApi.getStudentById(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<StudentModel>> call,
                                   @NonNull Response<RootResponse<StudentModel>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    StudentModel student = response.body().getData();
                    tvStudentNumber.setText(student.getStudentNumber() != null ? student.getStudentNumber() : "");
                    tvGrade.setText(student.getYearOfStudy() != null ? String.valueOf(student.getYearOfStudy()) : "");
                    if (student.getDepartment() != null) {
                        tvDepartment.setText(student.getDepartment().getDepartmentName());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<StudentModel>> call, @NonNull Throwable t) {
                Log.e("ProfileFragment", "Failed to fetch student details", t);
            }
        });
    }

    private void fetchLecturerProfile(UUID userId) {
        LecturerApi lecturerApi = ApiClient.getClient(getContext()).create(LecturerApi.class);
        lecturerApi.getLecturerById(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<LecturerModel>> call,
                                   @NonNull Response<RootResponse<LecturerModel>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    LecturerModel lecturer = response.body().getData();
                    
                    String prefix = "";
                    if (lecturer.getLecturerTitle() != null) {
                        int titleResId = -1;
                        switch (lecturer.getLecturerTitle()) {
                            case PROFESSOR: titleResId = R.string.TITLE_PROF_DR; break;
                            case ASSOCIATE_PROFESSOR: titleResId = R.string.TITLE_ASSOC_PROF; break;
                            case ASSISTANT_PROFESSOR: titleResId = R.string.TITLE_ASST_PROF; break;
                            case DOCTOR_LECTURER: titleResId = R.string.TITLE_DR; break;
                            case INSTRUCTOR: titleResId = R.string.TITLE_INSTRUCTOR; break;
                            case RESEARCH_ASSISTANT: titleResId = R.string.TITLE_RESEARCH_ASST; break;
                            default: break;
                        }
                        if (titleResId != -1) {
                            prefix = getString(titleResId) + " ";
                        }
                    }
                    
                    String fullName = prefix + tvFullName.getText().toString();
                    tvFullName.setText(fullName);

                    tvStudentNumber.setText(lecturer.getExtPhone() != null ? lecturer.getExtPhone() : "");
                    
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("tr"));
                    if (lecturer.getCreatedAt() != null) {
                        tvGrade.setText(lecturer.getCreatedAt().format(dtf));
                    }
                    
                    if (lecturer.getDepartment() != null) {
                        tvDepartment.setText(lecturer.getDepartment().getDepartmentName());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<LecturerModel>> call, @NonNull Throwable t) {
                Log.e("ProfileFragment", "Failed to fetch lecturer details", t);
            }
        });
    }
}