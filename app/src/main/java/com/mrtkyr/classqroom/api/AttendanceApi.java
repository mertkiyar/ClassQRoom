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
    @GET("rest/api/attendance/session/get/{id}")
    Call<RootResponse<AttendanceSessionModel>> getAttendanceSession(@Path("id") String uuid);

    @POST("rest/api/attendance/record/save")
    Call<RootResponse<Void>> takeAttendance(@Body AttendanceRecordModel record);

    @POST("rest/api/attendance/start")
    Call<RootResponse<AttendanceModel>> startAttendance(@Body AttendanceModel attendance);

    @GET("rest/api/attendance/session/current/{attendanceId}")
    Call<RootResponse<AttendanceSessionModel>> getCurrentSession(@Path("attendanceId") String attendanceId);

    @GET("rest/api/attendance/session/get-nfc/{nfcPath}")
    Call<RootResponse<AttendanceSessionModel>> getAttendanceSessionByNfcPath(@Path("nfcPath") String nfcPath);

    @GET("rest/api/attendance/session/get-code/{code}")
    Call<RootResponse<AttendanceSessionModel>> getAttendanceSessionByCode(@Path("code") String code);

    @GET("rest/api/attendance/record/student/{studentId}")
    Call<RootResponse<java.util.List<AttendanceRecordModel>>> getAttendanceRecordsByStudent(@Path("studentId") String studentId);

    @GET("rest/api/attendance/record/lecturer/{lecturerId}")
    Call<RootResponse<java.util.List<AttendanceRecordModel>>> getAttendanceRecordsByLecturer(@Path("lecturerId") String lecturerId);
}
