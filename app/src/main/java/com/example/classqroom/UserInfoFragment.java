package com.example.classqroom;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserInfoFragment extends Fragment {

    private OnNextClickListener onNextClickListener;

    public interface OnNextClickListener {
        void onNextClicked(String name, String surname, String email, String studentNumber);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtSurname = view.findViewById(R.id.edtSurname);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        Button btnCancelRegister = view.findViewById(R.id.btnCancelRegister);
        Button btnNext = view.findViewById(R.id.btnNext);

        btnCancelRegister.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnNext.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String surname = edtSurname.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String studentNumber = ((EditText) view.findViewById(R.id.edtStudentNumber)).getText().toString().trim();

            if (onNextClickListener != null) {
                onNextClickListener.onNextClicked(name, surname, email, studentNumber);
            }
        });
        edtName.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(24), //TODO ileride artırılabilir.
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
    }

    public void setOnNextClickListener(OnNextClickListener listener) {
        this.onNextClickListener = listener;
    }
}
