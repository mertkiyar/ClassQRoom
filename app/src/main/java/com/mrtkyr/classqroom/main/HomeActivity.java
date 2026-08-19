package com.mrtkyr.classqroom.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.fragment.student.NFCScannerFragment;
import com.mrtkyr.classqroom.fragment.lecturer.NFCWriterFragment;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ImageButton btnToggleDarkMode;
    ProgressBar progressBar;
    TextView tvWelcome;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LecturerViewPagerAdapter lecturerViewPagerAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserApi userApi = ApiClient.getClient(HomeActivity.this).create(UserApi.class);
        userApi.me().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RootResponse<UserModel>> call, @NonNull Response<RootResponse<UserModel>> response) {
                if (response.body() != null) {
                    String userType = response.body().getData().getUserType();
                    btnToggleDarkMode = findViewById(R.id.btnToggleDarkMode);
                    progressBar = findViewById(R.id.progressBar);
                    tvWelcome = findViewById(R.id.tvWelcome);
                    viewPager = findViewById(R.id.view_pager);
                    bottomNavigationView = findViewById(R.id.bottom_navigation);
                    switch (userType) {
                        case "ADMIN":
                        case "LECTURER":
                            lecturerViewPagerAdapter = new LecturerViewPagerAdapter(HomeActivity.this);
                            viewPager.setAdapter(lecturerViewPagerAdapter);
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.lecturer_menu_navigation);
                            setupLecturerNavigationListener();
                            break;
                        case "STUDENT":
                        default:
                            viewPagerAdapter = new ViewPagerAdapter(HomeActivity.this);
                            viewPager.setAdapter(viewPagerAdapter);
                            bottomNavigationView.getMenu().clear();
                            bottomNavigationView.inflateMenu(R.menu.menu_navigation);
                            setupNavigationListener();
                            break;
                    }
                    tvWelcome.setText(getString(R.string.TEXT_WELCOME, response.body().getData().getFirstName()));
                    progressBar.setVisibility(View.GONE);
                    btnToggleDarkMode.setVisibility(View.VISIBLE);
                    tvWelcome.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);

                    btnToggleDarkMode.setOnClickListener(v -> toggleDarkMode());
                } else {
                    handleDataError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RootResponse<UserModel>> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, getString(R.string.ERROR_NOT_TAKEN_USER_INFO), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setContentView(R.layout.activity_home);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle(getString(R.string.BUTTON_CONFIRM))
                        .setMessage(getString(R.string.MSG_QUIT))
                        .setPositiveButton(getString(R.string.BUTTON_YES), (dialog, which) -> finish())
                        .setNegativeButton(getString(R.string.BUTTON_NO), null)
                        .show();
            }
        });
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        if (!NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            return;
        }
        int currentItemPosition = viewPager.getCurrentItem();

        if (currentItemPosition == 1) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof NFCScannerFragment && fragment.isVisible()) {
                    ((NFCScannerFragment) fragment).handleNfcIntent(intent);
                    break;
                }
            }
        }
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag("nfc_writer");

            if (fragment instanceof NFCWriterFragment) {
                Tag tag;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag.class);
                } else {
                    //noinspection deprecation
                    tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                }
                if (tag != null) {
                    ((NFCWriterFragment) fragment).onNfcTagReceived(tag);
                }
            }
        }
    }


    private void handleDataError() {
        Toast.makeText(HomeActivity.this, getString(R.string.ERROR_NOT_TAKEN_USER_INFO), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setupNavigationListener() {
        if (bottomNavigationView == null) return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_qr_code) {
                viewPager.setCurrentItem(0, true);
            } else if (itemId == R.id.nav_nfc) {
                viewPager.setCurrentItem(1, true);
            } else if (itemId == R.id.nav_code) {
                viewPager.setCurrentItem(2, true);
            } else if (itemId == R.id.nav_history) {
                viewPager.setCurrentItem(3, true);
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(4, true);
            }
            return true;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_qr_code).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_nfc).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_code).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.nav_history).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }
        });
    }

    private void setupLecturerNavigationListener() {
        if (bottomNavigationView == null) return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_qr_code) {
                viewPager.setCurrentItem(0, true);
            } else if (itemId == R.id.nav_history) {
                viewPager.setCurrentItem(1, true);
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(2, true);
            }
            return true;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_qr_code).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_history).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }
        });
    }
    private void toggleDarkMode() {
        int newNightMode;
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else {
            newNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        }

        AppCompatDelegate.setDefaultNightMode(newNightMode);

        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme_mode", newNightMode);
        editor.apply();
    }
}
