package com.example.classqroom;

import android.os.Bundle;
import android.content.Intent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class PasswordFragment extends Fragment {

    private final String name;
    private final String surname;
    private final String email;
    private final String studentNumber;
    private OnRegisterClickListener onRegisterClickListener;

    public interface OnRegisterClickListener {
        void onRegisterClicked(String name, String surname, String email, String studentNumber, String password, String passwordConf);
    }

    public PasswordFragment(String name, String surname, String email, String studentNumber) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.studentNumber = studentNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText edtPassword = view.findViewById(R.id.edtPassword);
        EditText edtPasswordConf = view.findViewById(R.id.edtPasswordConf);

        Button btnPrevious = view.findViewById(R.id.btnPrevious);
        Button btnRegister = view.findViewById(R.id.btnRegister);

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
        btnRegister.setOnClickListener(v -> {
            String password = edtPassword.getText().toString().trim();
            String passwordConf = edtPasswordConf.getText().toString().trim();

            if (onRegisterClickListener != null) {
                onRegisterClickListener.onRegisterClicked(name, surname, email, studentNumber, password, passwordConf);
            }
        });
        edtPassword.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        String allowedChars = "~!@#$%^&*()_-+={}[]|:;\"'<,>.?/";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && !allowedChars.contains(String.valueOf(character))) {
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
                        if (!allowedLettersOrDigits.contains(String.valueOf(character)) && !allowedChars.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    public void setOnRegisterClickListener(OnRegisterClickListener listener) {
        this.onRegisterClickListener = listener;
    }
}
