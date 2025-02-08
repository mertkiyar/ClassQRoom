package com.mrtkyr.classqroom.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.fragment.admin.AddLectureInfoFragment;
import com.mrtkyr.classqroom.fragment.admin.AddLectureSettingsFragment;
import com.mrtkyr.classqroom.model.LectureModel;
import com.mrtkyr.classqroom.model.LecturerModel;

import java.util.List;

public class AddLectureActivity extends AppCompatActivity implements AddLectureInfoFragment.OnNextClickListener, AddLectureSettingsFragment.OnAddClickListener {
    private DatabaseHelper databaseHelper;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlecture);

        databaseHelper = new DatabaseHelper(this);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            showAddLectureInfoFragment();
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

        List<LecturerModel> lecturer = databaseHelper.getAllLecturers();
        if (lecturer.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.firstlecturer))
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void showAddLectureInfoFragment() {
        AddLectureInfoFragment addLectureInfoFragment = new AddLectureInfoFragment();
        addLectureInfoFragment.setOnNextClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flAddLecture, addLectureInfoFragment)
                .commit();
    }

    private void showAddLectureSettingsFragment(String lectureName, String language, String credit, String acts, boolean isCompulsory, boolean isOnline) {
        AddLectureSettingsFragment addLectureSettingsFragment = new AddLectureSettingsFragment(lectureName, language, credit, acts, isCompulsory, isOnline);
        addLectureSettingsFragment.setOnAddClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                )
                .replace(R.id.flAddLecture, addLectureSettingsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNextClicked(String lectureName, String language, String credit, String acts, boolean isCompulsory, boolean isOnline) {
        LectureModel lectureModel;
        if (!lectureName.isEmpty() && !language.isEmpty() && !credit.isEmpty() && !acts.isEmpty()) {
            lectureModel = new LectureModel(lectureName, language, Integer.parseInt(credit), Integer.parseInt(acts), isCompulsory, isOnline);
            boolean lectureExist = databaseHelper.checkLectureByName(lectureModel.getLectureName());
            if (!lectureExist) {
                showAddLectureSettingsFragment(lectureName, language, credit, acts, isCompulsory, isOnline);
            } else {
                Toast.makeText(this, getString(R.string.lectureexist), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onAddClicked(String lectureName, String language, String credit, String acts, boolean isCompulsory, boolean isOnline,
                            String faculty, String department, String code, String lecturerUUID) {
        LectureModel lectureModel;
        if (databaseHelper != null) {
            try {
                if (!lectureName.isEmpty() && !language.isEmpty() && !credit.isEmpty() && !acts.isEmpty()
                        && !faculty.isEmpty() && !department.isEmpty() && !code.isEmpty() && !lecturerUUID.isEmpty()) {
                    lectureModel = new LectureModel(lectureName, code, lecturerUUID, language, databaseHelper.getDepartmentIdByName(department),
                            isOnline, isCompulsory, Integer.parseInt(acts), Integer.parseInt(credit));
                    boolean isExistByCode = databaseHelper.checkLectureByCode(lectureModel.getLectureCode());
                    if (!isExistByCode) {
                        boolean isAdded = databaseHelper.addNewLecture(lectureModel);
                        if (isAdded) {
                            setResult(RESULT_OK);
                            Toast.makeText(this, getString(R.string.addlecturesucces), Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else {
                            Toast.makeText(this, getString(R.string.erroraddlecture), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.lecturecodeexist), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
