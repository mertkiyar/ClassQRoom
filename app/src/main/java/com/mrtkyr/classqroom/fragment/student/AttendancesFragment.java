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

import com.mrtkyr.classqroom.ApiClient;
import com.mrtkyr.classqroom.R;
import com.mrtkyr.classqroom.api.AttendanceApi;
import com.mrtkyr.classqroom.api.UserApi;
import com.mrtkyr.classqroom.model.AttendanceRecordModel;
import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendancesFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    // private RecyclerView rvAttendances;
    private ListView lvAttendances;
    private ArrayList<String> attendances;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attendances, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        // rvAttendances = view.findViewById(R.id.rvAttendances);
        lvAttendances = view.findViewById(R.id.lvAttendances);
        attendances = new ArrayList<>();
        // adapter = new ArrayAdapter<>(
        // requireContext(),
        // R.layout.recyclerview_attendance,
        // R.id.textView_line,
        // attendances
        // );
        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                attendances);
        swipeRefreshLayout.setOnRefreshListener(this::fetchAttendances);

        lvAttendances.setAdapter(adapter);
        fetchAttendances();
    }

    private void fetchAttendances() {
        UserApi userApi = ApiClient.getClient(getContext()).create(UserApi.class);
        userApi.me().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<RootResponse<UserModel>> call,
                    @NonNull Response<RootResponse<UserModel>> response) {
                if (response.body() != null && response.body().getData() != null) {
                    String userType = response.body().getData().getUserType();
                    String userUUID = response.body().getData().getUserId().toString();
                    fetchHistoryForUser(userType, userUUID);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), getString(R.string.ERROR_NOT_TAKEN_USER_INFO), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<RootResponse<UserModel>> call,
                    @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fetchHistoryForUser(String userType, String userUUID) {
        AttendanceApi attendanceApi = ApiClient.getClient(getContext()).create(AttendanceApi.class);

        if (userType.equals("student") || userType.equals("STUDENT")) {
            attendanceApi.getAttendanceRecordsByStudent(userUUID).enqueue(new Callback<>() {
                @Override
                public void onResponse(
                        @NonNull Call<RootResponse<List<AttendanceRecordModel>>> call,
                        @NonNull Response<RootResponse<List<AttendanceRecordModel>>> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    attendances.clear();
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().isEmpty()) {
                            Toast.makeText(getContext(), getString(R.string.MSG_NO_ATTENDANCES), Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            for (AttendanceRecordModel record : response.body().getData()) {
                                String courseName = "Unknown Course";
                                if (record.getAttendanceSession() != null
                                        && record.getAttendanceSession().getAttendance() != null
                                        && record.getAttendanceSession().getAttendance().getCourse() != null) {
                                    courseName = record.getAttendanceSession().getAttendance()
                                            .getCourse().getCourseName();
                                }

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm",
                                        new Locale("tr"));
                                String formattedDate = record.getAttendAt() != null ? record.getAttendAt().format(dtf)
                                        : "";
                                boolean isLate = record.getLate() != null && record.getLate();
                                String displayText = courseName;
                                if (isLate) {
                                    displayText += " (" + getString(R.string.TEXT_LATE) + ")";
                                }
                                displayText += "\n   " + getString(R.string.TEXT_ATTENDANCE_DATE) + " " + formattedDate + "\n";
                                attendances.add(displayText);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(
                        @NonNull Call<RootResponse<List<AttendanceRecordModel>>> call,
                        @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_ATTENDANCE_LIST), Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } else if (userType.equals("lecturer") || userType.equals("LECTURER")) {
            attendanceApi.getAttendanceRecordsByLecturer(userUUID).enqueue(new Callback<>() {
                @Override
                public void onResponse(
                        @NonNull Call<RootResponse<List<AttendanceRecordModel>>> call,
                        @NonNull Response<RootResponse<List<AttendanceRecordModel>>> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    attendances.clear();
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().isEmpty()) {
                            Toast.makeText(getContext(), getString(R.string.MSG_NO_ATTENDANCES), Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            for (AttendanceRecordModel record : response.body().getData()) {
                                String studentName = "Unknown Student";
                                if (record.getStudent() != null) {
                                    studentName = record.getStudent().getFirstName() + " "
                                            + record.getStudent().getLastName();
                                }

                                String courseName = "Unknown Course";
                                if (record.getAttendanceSession() != null
                                        && record.getAttendanceSession().getAttendance() != null
                                        && record.getAttendanceSession().getAttendance().getCourse() != null) {
                                    courseName = record.getAttendanceSession().getAttendance().getCourse()
                                            .getCourseName();
                                }

                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", new Locale("tr"));
                                String formattedDate = record.getAttendAt() != null ? record.getAttendAt().format(dtf)
                                        : "";
                                boolean isLate = record.getLate() != null && record.getLate();
                                String displayText = studentName;
                                if (isLate) {
                                    displayText += " (" + getString(R.string.TEXT_LATE) + ")";
                                }
                                displayText += "\n   " + getString(R.string.TEXT_ATTENDANCE_DATE) + " " + formattedDate + "\n   "
                                        + courseName + "\n";
                                attendances.add(displayText);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(
                        @NonNull Call<RootResponse<List<AttendanceRecordModel>>> call,
                        @NonNull Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), getString(R.string.ERROR_FETCHING_ATTENDANCE_LIST), Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }
}