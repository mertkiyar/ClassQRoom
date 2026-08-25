package com.mrtkyr.classqroom.api;

import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.StudentModel;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StudentApi {

    @GET("students/{id}")
    Call<RootResponse<StudentModel>> getStudentById(@Path("id") UUID studentId);
}
