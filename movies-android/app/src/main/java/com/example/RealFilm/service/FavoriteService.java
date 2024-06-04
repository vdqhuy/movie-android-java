package com.example.RealFilm.service;

import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Favorite;
import com.example.RealFilm.model.Rate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteService {
    @GET("movies/{movieId}/favorites")
    Call<ApiResponse<List<Favorite>>>
    getMovieFavorite(@Path("movieId") Integer movieId);
}
