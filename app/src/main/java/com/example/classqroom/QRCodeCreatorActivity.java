package com.example.classqroom;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class QRCodeCreatorActivity extends AppCompatActivity implements LectureInfoFragment.OnNextClickLectureListener, LectureSettingsFragment.OnStartLectureClickListener {

    private FragmentManager fragmentManager;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodecreator);

        databaseHelper = new DatabaseHelper(this);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            showLectureInfoFragment();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
    }

    private void showLectureInfoFragment() {
        LectureInfoFragment lectureInfoFragment = new LectureInfoFragment();
        lectureInfoFragment.setOnNextClickLectureListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flQRCodeCreator, lectureInfoFragment)
                .commit();
    }

    private void showLectureSettingsFragment(String lectureName, String lectureSection, int numberOfLecture, String lectureJoinType) {
        LectureSettingsFragment lectureSettingsFragment = new LectureSettingsFragment(lectureName, lectureSection, numberOfLecture, lectureJoinType);
        lectureSettingsFragment.setOnStartLectureClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flQRCodeCreator, lectureSettingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNextClickedLecture(String lectureName, String lectureSection, int numberOfLecture, String lectureJoinType) {
        if (!lectureName.isEmpty() && !lectureSection.isEmpty() && numberOfLecture > 0 && !lectureJoinType.isEmpty()) {
            showLectureSettingsFragment(lectureName, lectureSection, numberOfLecture, lectureJoinType);
        }
    }

    @Override
    public void onStartLectureClick(String lectureName, String lectureSection, int numberOfLecture, String lectureJoinType, String renewTime, int scanningpercent, boolean isShowLateStudents, boolean isConfirmAutoLateStudents) {
        if (!lectureName.isEmpty() && !lectureSection.isEmpty() && numberOfLecture > 0 && !lectureJoinType.isEmpty() && !renewTime.isEmpty() && scanningpercent > 0) {
            String lectureCode = databaseHelper.getLectureCode(lectureName);
            String section = lectureSection.toUpperCase();
            int sectionInteger = sectionToNumber(section);

        }
    }

    int sectionToNumber(String section) {
        if (section.contains("1")) {
            return 1;
        }else if (section.contains("2")) {
            return 2;
        }else {
            return -1;
        }
    }
}
