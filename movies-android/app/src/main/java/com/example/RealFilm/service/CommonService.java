package com.example.RealFilm.service;

import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Country;
import com.example.RealFilm.model.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommonService {

    @GET("genres")
    Call<ApiResponse<List<Genre>>> getGenres();

    @GET("countries")
    Call<ApiResponse<List<Country>>> getCountries();


}
