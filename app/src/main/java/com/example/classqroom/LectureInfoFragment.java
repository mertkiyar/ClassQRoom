package com.example.classqroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LectureInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lectureinfo, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SeekBar sbQRCreatiomTime = view.findViewById(R.id.sbQRCreatiomTime);
        SeekBar sbQRUsingLimit = view.findViewById(R.id.sbQRUsingLimit);
        CheckBox cbIsShowLateStudents = view.findViewById(R.id.cbIsShowLateStudents);
        CheckBox cbIsConfirmAutoLateStudents = view.findViewById(R.id.cbIsConfirmAutoLateStudents);

        sbQRCreatiomTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        sbQRUsingLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    }
    public void setOnClickListener() {

    }
}