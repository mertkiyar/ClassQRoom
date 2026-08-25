package com.mrtkyr.classqroom.api;

import com.mrtkyr.classqroom.model.AttendanceModel;
import com.mrtkyr.classqroom.model.AttendanceRecordModel;
import com.mrtkyr.classqroom.model.AttendanceSessionModel;
import com.mrtkyr.classqroom.model.RootResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AttendanceApi {
    @GET("attendance-sessions/{id}")
    Call<RootResponse<AttendanceSessionModel>> getAttendanceSession(@Path("id") String uuid);

    @POST("attendance-records")
    Call<RootResponse<Void>> takeAttendance(@Body AttendanceRecordModel record);

    @POST("attendances")
    Call<RootResponse<AttendanceModel>> startAttendance(@Body AttendanceModel attendance);

    @GET("attendance-sessions/current/{attendanceId}")
    Call<RootResponse<AttendanceSessionModel>> getCurrentSession(@Path("attendanceId") String attendanceId);

    @GET("attendance-sessions/nfc/{nfcPath}")
    Call<RootResponse<AttendanceSessionModel>> getAttendanceSessionByNfcPath(@Path("nfcPath") String nfcPath);

    @GET("attendance-sessions/code/{code}")
    Call<RootResponse<AttendanceSessionModel>> getAttendanceSessionByCode(@Path("code") String code);

    @GET("attendance-records/students/{studentId}")
    Call<RootResponse<java.util.List<AttendanceRecordModel>>> getAttendanceRecordsByStudent(@Path("studentId") String studentId);

    @GET("attendance-records/lecturers/{lecturerId}")
    Call<RootResponse<java.util.List<AttendanceRecordModel>>> getAttendanceRecordsByLecturer(@Path("lecturerId") String lecturerId);
}
