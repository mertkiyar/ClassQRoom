package com.mrtkyr.classqroom.fragment.lecturer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;

import java.util.List;

public class LectureInfoFragment extends Fragment {
    private OnNextClickLectureListener onNextClickLectureListener;

    public interface OnNextClickLectureListener {
        void onNextClickedLecture(String lectureName, String lectureSection, int numberOfLecture, String lectureJoinType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lectureinfo, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try (DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext())){
            String userUUID = null;
            if (getArguments() != null) {
                userUUID = getArguments().getString("USER_UUID");
            }

            if (userUUID == null) {
                Log.e("LectureInfoFragment", "USER_UUID is null");
                return;
            }

            Spinner spinLectureName = view.findViewById(R.id.spinLectureName);
            List<String> lectures = databaseHelper.getLecturesOfLecturer(userUUID);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, lectures);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinLectureName.setAdapter(adapter);
            spinLectureName.setSelection(0);

            Spinner spinLectureSection = view.findViewById(R.id.spinLectureSection);
            SeekBar sbNumberOfLecture = view.findViewById(R.id.sbNumberOfLecture);
            Spinner spinLectureJoinType = view.findViewById(R.id.spinLectureJoinType);
            Button btnCancelLectureInfo = view.findViewById(R.id.btnCancelLectureInfo);
            Button btnNextLectureInfo = view.findViewById(R.id.btnNextLectureInfo);

            btnCancelLectureInfo.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });

            btnNextLectureInfo.setOnClickListener(v -> {
                String lectureName = spinLectureName.getSelectedItem().toString();
                String lectureSection = spinLectureSection.getSelectedItem().toString();
                int numberOfLecture = sbNumberOfLecture.getProgress();
                String lectureJoinType = spinLectureJoinType.getSelectedItem().toString();

                if (onNextClickLectureListener != null) {
                    onNextClickLectureListener.onNextClickedLecture(lectureName, lectureSection, numberOfLecture, lectureJoinType);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setOnNextClickLectureListener(OnNextClickLectureListener listener) {
        this.onNextClickLectureListener = listener;
    }
}