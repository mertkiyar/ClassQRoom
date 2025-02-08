package com.mrtkyr.classqroom.fragment.admin;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.mrtkyr.classqroom.R;

public class AddLectureInfoFragment extends Fragment {
    private OnNextClickListener onNextClickListener;
    private EditText edtLectureName;
    private Spinner spinLanguage, spincredit, spinacts;
    private SwitchCompat switchCompulsory, switchOnline;

    public interface OnNextClickListener {
        void onNextClicked(String lectureName, String language, String credit, String acts, boolean isCompulsory, boolean isOnline);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addlectureinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtLectureName = view.findViewById(R.id.edtLectureName);
        spinLanguage = view.findViewById(R.id.spinLanguage);
        spincredit = view.findViewById(R.id.spincredit);
        spinacts = view.findViewById(R.id.spinacts);
        switchCompulsory = view.findViewById(R.id.switchCompulsory);
        switchOnline = view.findViewById(R.id.switchOnline);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnNext = view.findViewById(R.id.btnNext);

        btnCancel.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        if (getContext() != null) {
            String[] languages = new String[] {
                    getString(R.string.selectlang), getString(R.string.english), getString(R.string.turkish),
                    getString(R.string.german), getString(R.string.french), getString(R.string.italian),
                    getString(R.string.spanish), getString(R.string.portuguese), getString(R.string.russian)
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, languages);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinLanguage.setAdapter(adapter);

            String[] credits = new String[] {
                    getString(R.string.selectcredit), "1", getString(R.string.xcredits, "2"), getString(R.string.xcredits, "3"),
                    getString(R.string.xcredits, "4"), getString(R.string.xcredits, "5"), getString(R.string.xcredits, "6")
            }; //TODO I will add "credit" text this block of code. Just now it is not working properly. And I will add acts like this.

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, credits);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spincredit.setAdapter(adapter1);

            String[] acts = new String[] {
                    getString(R.string.selectacts), getString(R.string.xacts, "1"), getString(R.string.xacts, "2"), getString(R.string.xacts, "3"),
                    getString(R.string.xacts, "4"), getString(R.string.xacts, "5"), getString(R.string.xacts, "6"), getString(R.string.xacts, "7"),
                    getString(R.string.xacts, "8"), getString(R.string.xacts, "9"), getString(R.string.xacts, "10"), getString(R.string.xacts, "11"),
                    getString(R.string.xacts, "12"), getString(R.string.xacts, "13"), getString(R.string.xacts, "14"), getString(R.string.xacts, "15")
            };

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, acts);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinacts.setAdapter(adapter2);
        }

        btnNext.setOnClickListener(v -> {
            String lectureName = edtLectureName.getText().toString().trim();
            String language = spinLanguage.getSelectedItem().toString().trim();
            String credit = spincredit.getSelectedItem().toString().trim();
            String acts = spinacts.getSelectedItem().toString().trim();
            boolean isCompulsory = switchCompulsory.isChecked();
            boolean isOnline = switchOnline.isChecked();

            boolean isValid = true;

            if (lectureName.isEmpty()) {
                edtLectureName.setError(getString(R.string.cannotbeempty));
                isValid = false;
            } else if (lectureName.length() < 2) {
                edtLectureName.setError(getString(R.string.mintwochar));
                isValid = false;
            }

            if (language.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else if (language.equals(getString(R.string.selectlang))) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (credit.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else if (credit.equals(getString(R.string.selectcredit))) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (acts.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else if (acts.equals(getString(R.string.selectacts))) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (isValid && onNextClickListener != null) {
                onNextClickListener.onNextClicked(lectureName, language, credit, acts, isCompulsory, isOnline);
            }
        });

        edtLectureName.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(48),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        if (!Character.isLetter(character) && !Character.isSpaceChar(character)) {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    public void setOnNextClickListener(OnNextClickListener listener) {
        onNextClickListener = listener;
    }
}
