package com.mrtkyr.classqroom.api;

import com.mrtkyr.classqroom.model.RootResponse;
import com.mrtkyr.classqroom.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("/rest/api/user/me")
    Call<RootResponse<UserModel>> me();

}
