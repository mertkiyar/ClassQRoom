package com.mrtkyr.classqroom.fragment.lecturer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtkyr.classqroom.R;

public class LectureSettingsFragment extends Fragment {
    private final String lectureName;
    private final String lectureSection;
    private final int numberOfLecture;
    private final String lectureJoinType;
    private OnStartLectureClickListener onStartLectureClickListener;

    public interface OnStartLectureClickListener {
        void onStartLectureClick(String lectureName, String lectureSection, int numberOfLecture, String lectureJoinType, String creationTime, int usingLimit, boolean isShowLateStudents, boolean isConfirmAutoLateStudents);
    }

    public LectureSettingsFragment(String lectureName, String lectureSection, int numberOfLecture, String lectureJoinType) {
        this.lectureName = lectureName;
        this.lectureSection = lectureSection;
        this.numberOfLecture = numberOfLecture;
        this.lectureJoinType = lectureJoinType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lecturesettings, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userUUID = null;
        if (getArguments() != null) {
            userUUID = getArguments().getString("USER_UUID");
        }

        if (userUUID == null) {
            Log.e("LectureSettingsFragment", "USER_UUID is null");
            return;
        }

        SeekBar sbQRRenewTime = view.findViewById(R.id.sbQRRenewTime);
        SeekBar sbQRScanningMaxPercentagePerQR = view.findViewById(R.id.sbQRScanningMaxPercentagePerQR);
        CheckBox cbIsShowLateStudents = view.findViewById(R.id.cbIsShowLateStudents);
        CheckBox cbIsConfirmAutoLateStudents = view.findViewById(R.id.cbIsConfirmAutoLateStudents);
        Button btnCancelLectureSettings = view.findViewById(R.id.btnCancelLectureSettings);
        Button btnStartLecture = view.findViewById(R.id.btnStartLecture);

        sbQRRenewTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getContext(), "Time: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sbQRScanningMaxPercentagePerQR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getContext(), "Limit: " + progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        cbIsShowLateStudents.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = isChecked ? "Filter Last Student Enabled" : "Filter Last Student Disabled";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        cbIsConfirmAutoLateStudents.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = isChecked ? "Auto Confirm Enabled" : "Auto Confirm Disabled";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        btnCancelLectureSettings.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnStartLecture.setOnClickListener(v -> {
            String creationTime = String.valueOf(sbQRRenewTime.getProgress());
            int usingLimit = sbQRScanningMaxPercentagePerQR.getProgress();
            boolean isShowLateStudents = cbIsShowLateStudents.isChecked();
            boolean isConfirmAutoLateStudents = cbIsConfirmAutoLateStudents.isChecked();

            if (onStartLectureClickListener != null) {
                onStartLectureClickListener.onStartLectureClick(lectureName, lectureSection, numberOfLecture, lectureJoinType, creationTime, usingLimit, isShowLateStudents, isConfirmAutoLateStudents);
            }
        });
    }
    public void setOnStartLectureClickListener(OnStartLectureClickListener listener) {
        this.onStartLectureClickListener = listener;
    }
}
