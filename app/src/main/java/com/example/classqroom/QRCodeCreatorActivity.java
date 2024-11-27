package com.example.classqroom;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class QRCodeCreatorActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodecreator);

        fragmentManager = getSupportFragmentManager();
        databaseHelper = new DatabaseHelper(this);

        if (savedInstanceState == null) {
            //TODO
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
//        lectureInfoFragment.setOnNextClickListener(this);

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

    //TODO showLectureSettingsFragment();

    public void onNextClicked(){

    }
}
