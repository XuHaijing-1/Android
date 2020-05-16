package com.example.diandian;


import android.provider.ContactsContract;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/user/login/{username}/{password}")
    Call<Result> login(@Path("username") String username,@Path("password") String password);

    @POST("/user/register")
    Call<User> register(@Body User user);
}
