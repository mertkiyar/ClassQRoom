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

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtkyr.classqroom.R;

import java.util.HashMap;

public class CodeFragment extends Fragment {
    private FirebaseFirestore db;
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private EditText[] codeBoxes;

    public static CodeFragment newInstance(String userUID) {
        CodeFragment fragment = new CodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserUID = getArguments().getString(ARG_USER_UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
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

        db.collection("codes")
                        .document(enteredCode)
                                .get()
                                        .addOnCompleteListener(codesTask -> {
                                            DocumentSnapshot codeDoc = codesTask.getResult();
                                            if (!codesTask.isSuccessful() || !codeDoc.exists()) {
                                                Toast.makeText(getContext(), getString(R.string.MSG_CODE_INVALID), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            String lectureUID = codeDoc.getString("lectureUID");
                                            String sessionUID = codeDoc.getString("sessionUID");
                                            if (lectureUID == null || sessionUID == null) return;
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
                                                                                attendance.put("type", "6-Digit Code");

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
                                                                                            }
                                                                                        });
                                                                            } else {
                                                                                if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }).addOnFailureListener(lectureError -> {
                                                                            if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                                        });
                                                                    } else {
                                                                        if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }).addOnFailureListener(userError -> {
                                                                    if (getContext() != null) Toast.makeText(getContext(), getString(R.string.ERROR_ATTEND_LECTURE), Toast.LENGTH_SHORT).show();
                                                                });
                                                            } else {
                                                                Toast.makeText(getContext(), getString(R.string.MSG_CODE_INVALID), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
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