package com.mrtkyr.classqroom.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.main.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AddLecturerPassFragment extends Fragment {
    private final String name;
    private final String surname;
    private final String email;
    private final String department;
    private final String title;
    private OnAddClickListener onAddClickListener;
    private final Random random = new Random();

    public interface OnAddClickListener {
        void onAddClicked(String name, String surname, String email, String department,
                          String title, String password, String passwordConf);
    }

    public AddLecturerPassFragment(String name, String surname, String email, String department, String title) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.department = department;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addlecturerpass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText edtPassword = view.findViewById(R.id.edtPassword);
        EditText edtPasswordConf = view.findViewById(R.id.edtPasswordConf);
        TextView tvGeneratePassword = view.findViewById(R.id.tvGeneratePassword);
        Button btnPrevious = view.findViewById(R.id.btnPrevious);
        Button btnAdd = view.findViewById(R.id.btnAdd);

        btnPrevious.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            } else {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        btnAdd.setOnClickListener(v -> {
            String password = edtPassword.getText().toString().trim();
            String passwordConf = edtPasswordConf.getText().toString().trim();

            if (onAddClickListener != null) {
                onAddClickListener.onAddClicked(name, surname, email, department, title, password, passwordConf);
            }
        });

        tvGeneratePassword.setOnClickListener(v -> {
            tvGeneratePassword.setEnabled(false);
            tvGeneratePassword.postDelayed(() -> tvGeneratePassword.setEnabled(true), 500);
            String password = generateRandomPassword();
            edtPassword.setText(password);
            edtPasswordConf.setText(password);
        });

        edtPassword.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        String allowedChars = "~!@#$%^&*()_-+={}[]|:;\"'<,>.?/";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character))
                                && !allowedChars.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        edtPasswordConf.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        String allowedChars = "~!@#$%^&*()_-+={}[]|:;\"'<,>.?/";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character))
                                && !allowedChars.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        this.onAddClickListener = listener;
    }

    private String generateRandomPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "~!@#$%^&*()-_=+[{]}|;:'\",<.>/?";

        String allCharacters = upperCase + lowerCase + digits + specialCharacters;

        StringBuilder password = new StringBuilder();
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < 12; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }

        Collections.shuffle(passwordChars);

        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }
        return finalPassword.toString();
    }
}
