package com.example.RealFilm.service;

import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {


    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse<User>>
    login(@Field("email") String email,
          @Field("password") String password);


    @FormUrlEncoded
    @POST("signup")
    Call<ApiResponse>
    signup(@Field("name") String name,
           @Field("password") String password,
           @Field("email") String email,
           @Field("birthday") String birthday);


    @GET("profile")
    Call<ApiResponse<User>> getUser();

    @FormUrlEncoded
    @PUT("profile")
    Call<ApiResponse<User>>
    updateUser(@Field("name") String name,
               @Field("email") String email,
               @Field("birthday") String birthday,
               @Field("photoURL") String photoURL);
}
