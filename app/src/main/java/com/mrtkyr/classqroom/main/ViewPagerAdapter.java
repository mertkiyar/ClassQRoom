package com.mrtkyr.classqroom.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mrtkyr.classqroom.fragment.student.AttendancesFragment;
import com.mrtkyr.classqroom.fragment.student.CodeFragment;
import com.mrtkyr.classqroom.fragment.student.NFCFragment;
import com.mrtkyr.classqroom.fragment.student.ProfileFragment;
import com.mrtkyr.classqroom.fragment.student.QrCodeScannerFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final String userUID;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String userUID) {
        super(fragmentActivity);
        this.userUID = userUID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 1 -> new NFCFragment();
            case 2 -> new CodeFragment();
            case 3 -> new AttendancesFragment();
            case 4 -> new ProfileFragment();
            default -> new QrCodeScannerFragment();
        };
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
