package com.mrtkyr.classqroom.api;

import com.mrtkyr.classqroom.model.CourseModel;
import com.mrtkyr.classqroom.model.RootResponse;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LecturerApi {

    @GET("/rest/api/lecturerCourse/lecturer/{id}/courses")
    Call<RootResponse<List<CourseModel>>> getCoursesByLecturer(@Path("id") UUID lecturerId);
}
