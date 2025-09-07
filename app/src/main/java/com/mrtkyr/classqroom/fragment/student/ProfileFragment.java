package com.mrtkyr.classqroom.fragment.student;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.main.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private FirebaseFirestore db;
    private TextView tvFullName;
    private TextView tvStudentNumber;
    private TextView tvDepartment;
    private TextView tvGrade;
    private ImageView ivProfile;

    public static ProfileFragment newInstance(String userUID) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            this.mUserUID = getArguments().getString(ARG_USER_UID);
        }
        db = FirebaseFirestore.getInstance();
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
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    })
                    .setNegativeButton(getString(R.string.BUTTON_NO), null)
                    .show();
        });
        getData();
    }

    private void getData() {
        db.collection("users")
                .document(mUserUID)
                .get()
                .addOnSuccessListener(task -> {
                    if (Objects.equals(task.getString("userType"), "student")) {
                        if (Objects.equals(task.getString("gender"), "male")) {
                            ivProfile.setImageResource(R.drawable.img_male_student);
                        } else if (Objects.equals(task.getString("gender"), "female")) {
                            ivProfile.setImageResource(R.drawable.img_female_student);
                        }

                        db.collection("students")
                                .document(mUserUID)
                                .get()
                                .addOnSuccessListener(studentTask -> {
                                    Long number = studentTask.getLong("number");
                                    tvStudentNumber.setText(String.valueOf(number));
                                    Long grade = studentTask.getLong("grade");
                                    tvGrade.setText(String.valueOf(grade));
                                });
                        String fullName = task.getString("name") + " " + task.getString("surname");
                        tvFullName.setText(fullName);
                    }

                    else if (Objects.equals(task.getString("userType"), "lecturer")) {
                        if (Objects.equals(task.getString("gender"), "male")) {
                            ivProfile.setImageResource(R.drawable.img_male_lecturer);
                        } else if (Objects.equals(task.getString("gender"), "female")) {
                            ivProfile.setImageResource(R.drawable.img_female_lecturer);
                        }

                        db.collection("lecturers")
                                .document(mUserUID)
                                .get()
                                .addOnSuccessListener(lecturerTask -> {
                                    String title = lecturerTask.getString("title");

                                    String fullName = title + " " + task.getString("name") + " " + task.getString("surname");
                                    tvFullName.setText(fullName);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", new Locale("tr"));
                                    String formattedDate = sdf.format(Objects.requireNonNull(lecturerTask.getTimestamp("employmentStartDate")).toDate());
                                    tvGrade.setText(formattedDate);

                                    tvStudentNumber.setText(String.valueOf(lecturerTask.getLong("lecturerNumber")));
                                });
                    }
                    try {
                        db.collection("departments")
                                .document(Objects.requireNonNull(task.getString("departmentUID")))
                                .get()
                                .addOnCompleteListener(departmentTask -> {
                                    if (departmentTask.isSuccessful()) {
                                        tvDepartment.setText(departmentTask.getResult().getString("name"));
                                    }
                                });
                    } catch (Exception e){
                        Log.e("ProfileFragment", Objects.requireNonNull(e.getMessage()));
                    }

                });
    }
}