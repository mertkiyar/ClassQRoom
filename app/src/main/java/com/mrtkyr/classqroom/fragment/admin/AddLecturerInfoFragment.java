package com.mrtkyr.classqroom.fragment.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.model.DepartmentModel;

import java.util.ArrayList;
import java.util.List;

public class AddLecturerInfoFragment extends Fragment {
    private OnNextClickListener onNextClickListener;
    private DatabaseHelper databaseHelper;

    public interface OnNextClickListener {
        void onNextClicked(String name, String surname, String email, String department, String title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addlecturerinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtSurname = view.findViewById(R.id.edtSurname);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        Spinner spinDepartment = view.findViewById(R.id.spinDepartment);
        Spinner spinTitle = view.findViewById(R.id.spinTitle);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnNext = view.findViewById(R.id.btnNext);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoFillEmail(edtName, edtSurname, edtEmail);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoFillEmail(edtName, edtSurname, edtEmail);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNext.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String surname = edtSurname.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String department = spinDepartment.getSelectedItem().toString().trim();
            String title = spinTitle.getSelectedItem().toString().trim();

            boolean isValid = true;

            if (name.isEmpty()) {
                edtName.setError(getString(R.string.cannotbeempty));
                isValid = false;
            } else if (name.length() < 2) {
                edtName.setError(getString(R.string.mintwochar));
                isValid = false;
            }

            if (surname.isEmpty()) {
                edtSurname.setError(getString(R.string.cannotbeempty));
                isValid = false;
            } else if (surname.length() < 2) {
                edtSurname.setError(getString(R.string.mintwochar));
                isValid = false;
            }

            if (email.isEmpty()) {
                edtEmail.setError(getString(R.string.cannotbeempty));
                isValid = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError(getString(R.string.invalidformat));
                isValid = false;
            }

            if (department.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else if (department.equals(getString(R.string.selectdepartment))) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (title.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else if (title.equals(getString(R.string.selecttitle))) {
                Toast.makeText(getContext(), getString(R.string.fillblanks), Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (isValid && onNextClickListener != null) {
                onNextClickListener.onNextClicked(name, surname, email, department, title);
            }
        });

        edtName.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        if (!Character.isLetter(character) && !Character.isSpaceChar(character)) {
                            return "";
                        }
                        if (Character.isSpaceChar(character)) {
                            if (dstart > 0 && Character.isSpaceChar(dest.charAt(dstart - 1))) {
                                return "";
                            }
                        }
                    }
                    return null;
                }
        });

        edtSurname.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        if (!Character.isLetter(character) && !Character.isSpaceChar(character)) {
                            return "";
                        }
                        if (Character.isSpaceChar(character)) {
                            if (dstart > 0 && Character.isSpaceChar(dest.charAt(dstart - 1))) {
                                return "";
                            }
                        }
                    }
                    return null;
                }
        });

        edtEmail.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(40),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && character != '@' && character != '.') {
                            return "";
                        }
                    }
                    return null;
                }
        });
        databaseHelper = new DatabaseHelper(getContext());
        List<DepartmentModel> departments = databaseHelper.getAllDepartments();
        List<String> departmentNames = new ArrayList<>();
        departmentNames.add(getString(R.string.selectdepartment));
        for (DepartmentModel department : departments) {
            departmentNames.add(department.getDepartmentName());
        }

        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, departmentNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinDepartment.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            return;
        }

        String[] titles = new String[] {
                getString(R.string.selecttitle), getString(R.string.instructor), getString(R.string.researchasst),
                getString(R.string.lecturer), getString(R.string.doctor), getString(R.string.asstprof),
                getString(R.string.assocprof), getString(R.string.profdr)
        };

        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, titles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinTitle.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            return;
        }

        spinTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setOnNextClickListener(OnNextClickListener onNextClickListener) {
        this.onNextClickListener = onNextClickListener;
    }

    private void autoFillEmail(EditText edtName, EditText edtSurname, EditText edtEmail) {
        String name = edtName.getText().toString().trim().toLowerCase();
        String surname = edtSurname.getText().toString().trim().toLowerCase();

        name = convertToEnglishLetters(name);
        surname = convertToEnglishLetters(surname);

        if (!name.isEmpty()) {
            String email = name + surname + "@unimail.edu"; //editit add settings(admin)
            edtEmail.setText(email);
        }
    }

    private String convertToEnglishLetters(String input) {
        String[] turkishLetters = {"Ç", "Ş", "Ğ", "Ü", "Ö", "İ", "ç", "ş", "ğ", "ü", "ö", "ı"};
        String[] englishLetters = {"C", "S", "G", "U", "O", "I", "c", "s", "g", "u", "o", "i"};

        for (int i = 0; i < turkishLetters.length; i++) {
            input = input.replace(turkishLetters[i], englishLetters[i]);
        }

        return input;
    }
}
