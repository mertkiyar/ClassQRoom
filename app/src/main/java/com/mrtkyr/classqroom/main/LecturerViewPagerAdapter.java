package com.mrtkyr.classqroom.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mrtkyr.classqroom.fragment.lecturer.StartLectureFragment;
import com.mrtkyr.classqroom.fragment.student.AttendancesFragment;
import com.mrtkyr.classqroom.fragment.student.ProfileFragment;

public class LecturerViewPagerAdapter extends FragmentStateAdapter{
    private final String userUID;

    public LecturerViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String userUID) {
        super(fragmentActivity);
        this.userUID = userUID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 1 -> AttendancesFragment.newInstance(userUID);
            case 2 -> ProfileFragment.newInstance(userUID);
            default -> new StartLectureFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
