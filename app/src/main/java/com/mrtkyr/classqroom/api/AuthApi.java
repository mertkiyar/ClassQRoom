package com.mrtkyr.classqroom.api;

import com.mrtkyr.classqroom.model.AuthRequest;
import com.mrtkyr.classqroom.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("authenticate")
    Call<AuthResponse> login(@Body AuthRequest request);
}
