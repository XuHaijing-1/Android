package com.example.diandian;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/user/login/{username}/{password}")
   Call<Response> login(@Path("username") String username,@Path("password") String password);
}
