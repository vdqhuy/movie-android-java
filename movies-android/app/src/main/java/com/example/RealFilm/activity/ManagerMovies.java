package com.example.RealFilm.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RealFilm.R;
import com.example.RealFilm.adapter.ManagerMovieAdapter;
import com.example.RealFilm.listerner.MovieDeleteListener;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerMovies extends AppCompatActivity implements MovieDeleteListener {

    // ...rest ui
    private List<Movie> movies;
    private RecyclerView recyclerView;
    private ImageButton btnBack;

    // ...rest different
    private ManagerMovieAdapter managerMovieAdapter;
    private LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_movies);

        // ...
        recyclerView = findViewById(R.id.list_movies);
        btnBack = findViewById(R.id.btn_back);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        // ...
        managerMovieAdapter = new ManagerMovieAdapter(movies, this);
        recyclerView.setAdapter(managerMovieAdapter);

        // ...rest api
        getMovies();

        // ...
        btnBackOnClick();
    }

    private void btnBackOnClick () {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void getMovies() {
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<List<Movie>>> call = movieService.getMoviesLatest();
        call.enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                if (response.isSuccessful()) {
                    List<Movie> moviesRes = response.body().getData();
                    if (moviesRes != null && !moviesRes.isEmpty()) {
                        movies = moviesRes;
                        managerMovieAdapter.setMovies(movies);
                        managerMovieAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("MANAGER_MOVIES", "Empty movie list");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {
                Log.e("MANAGER_MOVIES", "API call failed", t);
            }
        });
    }

    @Override
    public void onDeleteMovie(Integer movieId) {
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse> call = movieService.deleteMovie(movieId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    getMovies();

                } else {
                    Log.e("MANAGER_MOVIES", "Delete movie failed");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("MANAGER_MOVIES", "API call failed", t);
            }
        });

    }
}