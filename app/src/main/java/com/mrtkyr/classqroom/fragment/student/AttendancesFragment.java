package com.mrtkyr.classqroom.fragment.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mrtkyr.classqroom.main.Attendance;
import com.mrtkyr.classqroom.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class AttendancesFragment extends Fragment {
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeRefreshLayout;

    //    private RecyclerView rvAttendances;
    private ListView lvAttendances;
    private static final String ARG_USER_UID = "userUID";
    private String mUserUID;
    private ArrayList<String> attendances;
    private ArrayAdapter<String> adapter;
    public static AttendancesFragment newInstance(String userUID) {
        AttendancesFragment fragment = new AttendancesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_UID, userUID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attendances, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            this.mUserUID = getArguments().getString(ARG_USER_UID);
        }
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
//        rvAttendances = view.findViewById(R.id.rvAttendances);
        lvAttendances = view.findViewById(R.id.lvAttendances);
        db = FirebaseFirestore.getInstance();

        attendances = new ArrayList<>();
//        adapter = new ArrayAdapter<>(
//                requireContext(),
//                R.layout.recyclerview_attendance,
//                R.id.textView_line,
//                attendances
//        );
        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                attendances
        );
        swipeRefreshLayout.setOnRefreshListener(this::fetchAttendances);

        lvAttendances.setAdapter(adapter);
        fetchAttendances();
    }

    private void fetchAttendances() {
        db.collection("users")
                .document(mUserUID)
                .get()
                .addOnSuccessListener(task -> {
                    if (Objects.equals(task.getString("userType"), "student")) {
                        db.collection("attendances")
                                .whereEqualTo("studentUID", mUserUID)
                                .orderBy("scannedAt", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(studentTask -> {
                                    if (studentTask.isSuccessful()) {
                                        attendances.clear();
                                        if (studentTask.getResult().isEmpty()) {
                                            Toast.makeText(getContext(), getString(R.string.MSG_NO_ATTENDANCES), Toast.LENGTH_LONG).show();
                                        } else {
                                            for (DocumentSnapshot document : studentTask.getResult()) {
                                                Attendance attendance = document.toObject(Attendance.class);

                                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("tr"));
                                                assert attendance != null;
                                                String formattedDate = sdf.format(attendance.getScannedAt().toDate());

                                                String displayText = attendance.getLectureName()  + " (" + attendance.getStatus() + ")\n   "
                                                        + getString(R.string.TEXT_ATTENDANCE_DATE) + " " + formattedDate + "\n";
                                                attendances.add(displayText);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_ATTENDANCE_LIST), Toast.LENGTH_SHORT).show();
                                    }

                                    swipeRefreshLayout.setRefreshing(false);
                                });
                    } else if(Objects.equals(task.getString("userType"), "lecturer")) {
                        db.collection("attendances")
                                .whereEqualTo("lecturerUID", mUserUID)
                                .orderBy("scannedAt", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(lecturerTask -> {
                                    if (lecturerTask.isSuccessful()) {
                                        attendances.clear();
                                        if (lecturerTask.getResult().isEmpty()) {
                                            Toast.makeText(getContext(), getString(R.string.MSG_NO_ATTENDANCES), Toast.LENGTH_LONG).show();
                                        } else {
                                            for (DocumentSnapshot document : lecturerTask.getResult()) {
                                                Attendance attendance = document.toObject(Attendance.class);
                                                if (attendance == null) break;
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, HH:mm", new Locale("tr"));
                                                String formattedDate = sdf.format(attendance.getScannedAt().toDate());

                                                String displayText = attendance.getStudentName()  + " (" + attendance.getStatus() + ")\n   "
                                                        + getString(R.string.TEXT_ATTENDANCE_DATE) + " " + formattedDate + "\n   " + attendance.getLectureName() + "\n";
                                                attendances.add(displayText);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_ATTENDANCE_LIST), Toast.LENGTH_SHORT).show();
                                    }

                                    swipeRefreshLayout.setRefreshing(false);
                                });
                    }
                });
    }
}