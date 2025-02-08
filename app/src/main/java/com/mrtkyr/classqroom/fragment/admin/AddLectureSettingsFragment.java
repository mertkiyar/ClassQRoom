package com.mrtkyr.classqroom.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrtkyr.classqroom.DatabaseHelper;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.main.MainActivity;
import com.mrtkyr.classqroom.model.DepartmentModel;
import com.mrtkyr.classqroom.model.FacultyModel;
import com.mrtkyr.classqroom.model.LecturerModel;

import java.util.ArrayList;
import java.util.List;

public class AddLectureSettingsFragment extends Fragment {
    private String userUUID;
    private final String lectureName;
    private final String language;
    private final String credit;
    private final String acts;
    private final boolean isCompulsory;
    private final boolean isOnline;
    private OnAddClickListener onAddClickListener;
    final List<String> lecturerUUIDWrapper = new ArrayList<>();

    public interface OnAddClickListener {
        void onAddClicked(String lectureName, String language, String credit, String acts, boolean isCompulsory, boolean isOnline,
                          String faculty, String department, String code, String lecturer);
    }

    public AddLectureSettingsFragment(String lectureName, String language, String credit,
                                      String acts, boolean isCompulsory, boolean isOnline) {
        this.lectureName = lectureName;
        this.language = language;
        this.credit = credit;
        this.acts = acts;
        this.isCompulsory = isCompulsory;
        this.isOnline = isOnline;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addlecturesettings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinFaculty = view.findViewById(R.id.spinFaculty);
        EditText edtCode = view.findViewById(R.id.edtCode);
        Spinner spinDepartment = view.findViewById(R.id.spindepartment);
        Spinner spinLecturer = view.findViewById(R.id.spinLecturer);
        Button btnPrevious = view.findViewById(R.id.btnPrevious);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        List<Pair<String, String>> lecturerList = new ArrayList<>();

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

        if (getContext() != null) {
            try (DatabaseHelper databaseHelper = new DatabaseHelper(getContext())) {
                List<FacultyModel> faculties = databaseHelper.getAllFaculties();
                List<String> facultyNames = new ArrayList<>();
                facultyNames.add(getString(R.string.selectfaculty));
                for (FacultyModel faculty : faculties) {
                    facultyNames.add(faculty.getFacultyName());
                }
                ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, facultyNames);
                facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinFaculty.setAdapter(facultyAdapter);

                List<String> departmentNames = new ArrayList<>();
                departmentNames.add(getString(R.string.selectdepartment));
                ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, departmentNames);
                departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinDepartment.setAdapter(departmentAdapter);
                spinDepartment.setEnabled(false);

                List<String> lecturerNames = new ArrayList<>();
                lecturerNames.add(getString(R.string.selectlecturer));
                ArrayAdapter<String> lecturerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lecturerNames);
                lecturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinLecturer.setAdapter(lecturerAdapter);
                spinLecturer.setEnabled(false);

                spinFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (getContext() != null && position > 0 && position < facultyNames.size()) {
                            String selectedFacultyName = facultyNames.get(position);

                            int facultyId = databaseHelper.getFacultyIdByName(selectedFacultyName);

                            if (facultyId != -1) {
                                List<DepartmentModel> departments = databaseHelper.getDepartmentsByFacultyId(facultyId);
                                for (DepartmentModel department : departments) {
                                    departmentNames.add(department.getDepartmentName());
                                }
                                ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, departmentNames);
                                departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinDepartment.setAdapter(departmentAdapter);
                                spinDepartment.setEnabled(true);
                            } else {
                                spinDepartment.setEnabled(false);
                            }
                        } else {
                            spinDepartment.setSelection(0);
                            spinDepartment.setEnabled(false);
                            spinLecturer.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (getContext() != null && position > 0 && position <= departmentNames.size()) {
                            String selectedDepartmentName = departmentNames.get(position);
                            int departmentId = databaseHelper.getDepartmentIdByName(selectedDepartmentName);

                            if (departmentId != -1) {
                                spinLecturer.setEnabled(true);
                                String lectureCode = databaseHelper.getLectureCodeFromDepartments(selectedDepartmentName);
                                edtCode.setText(lectureCode);

                                List<LecturerModel> lecturer = databaseHelper.getLecturersByDepartmentId(departmentId);
                                lecturerList.add(new Pair<>(getString(R.string.selectlecturer), ""));

                                for (LecturerModel lecturerModel : lecturer) {
                                    String fullName = databaseHelper.getLecturerFullName(lecturerModel.getUuid());
                                    lecturerList.add(new Pair<>(fullName, lecturerModel.getUuid()));
                                }
                                List<String> lecturerNames = new ArrayList<>();
                                for (Pair<String, String> pair : lecturerList) {
                                    lecturerNames.add(pair.first);
                                }
                                ArrayAdapter<String> lecturerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lecturerNames);
                                lecturerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinLecturer.setAdapter(lecturerAdapter);
                            } else {
                                spinLecturer.setEnabled(false);
                            }
                        } else {
                            spinLecturer.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinLecturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            lecturerUUIDWrapper.add(lecturerList.get(position).second);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        btnAdd.setOnClickListener(v -> {
            String faculty = spinFaculty.getSelectedItem().toString().trim();
            String code = edtCode.getText().toString().trim();
            String department = spinDepartment.getSelectedItem().toString().trim();
            String lecturerUUID = lecturerUUIDWrapper.get(0);

            if (onAddClickListener != null) {
                onAddClickListener.onAddClicked(lectureName, language, credit, acts, isCompulsory, isOnline,
                        faculty, department, code, lecturerUUID);
            }
        });

        edtCode.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(8),
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        char character = source.charAt(i);
                        String allowedLettersOrDigits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                        if (!allowedLettersOrDigits.contains(String.valueOf(character))) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        try (DatabaseHelper databaseHelper = new DatabaseHelper(getContext())) {
            if (spinFaculty != null) {
                if (spinDepartment != null) {
                    String code = databaseHelper.getLectureCodeFromDepartments(spinDepartment.getSelectedItem().toString().trim());
                    if (code != null) {
                        edtCode.setText(code);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        this.onAddClickListener = listener;
    }
}
