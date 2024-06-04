package com.example.RealFilm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.fragment.CommentsFragment;
import com.example.RealFilm.fragment.RateStarFragment;
import com.example.RealFilm.fragment.RecommendFragment;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.model.Status;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.MovieService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesInformationActivity extends AppCompatActivity {

    // Lấy ID từ Intent

    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private TextView textView_review, btn_show_more_review, textView_tab_1, textView_tab_2, textView_tab_3,
            textView_name_movies, textView_category_movies, textView_cast_movies, textView_year_movies,
            textView_nation_movies, textView_director_movies, textView_time_movies, textView_count_rates,
            textView_view_count_movies;
    private LinearLayout tab_1, tab_2, tab_3, btn_trailer;
    private View line_4, line_5;
    private GridLayout gridlayout;
    private ImageView imageView_poser_3x4, imageView_poser_4x3, star_1, star_2, star_3, star_4, star_5, btn_favourite;
    private Button btn_play;
    private String name, trailer, category, year;
    private ImageButton btn_back;
    private boolean checkSH = false;
    private String str1 = "";
    private String str2 = "";
    private FirebaseUser user;
    private int STAR = 0, OTAL_RATING = 0;
    String link;
    String[] splits;
    Boolean hasLiked;

    Integer movieId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_information);


        movieId = getIntent().getIntExtra("id", 0);
        hasLiked = false;

        initUi();
        setValueMovies();

        btnShowHideReviewOnClick();
        btnBackOnClick();
        changeTab();
        btnFavouriteOnClick();
    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        textView_review = findViewById(R.id.textView_review);
        textView_name_movies = findViewById(R.id.textView_name_movies);
        textView_category_movies = findViewById(R.id.textView_category_movies);
        textView_cast_movies = findViewById(R.id.textView_cast_movies);
        textView_year_movies = findViewById(R.id.textView_year_movies);
        textView_nation_movies = findViewById(R.id.textView_nation_movies);
        textView_director_movies = findViewById(R.id.textView_director_movies);
        textView_time_movies = findViewById(R.id.textView_time_movies);
        textView_count_rates = findViewById(R.id.textView_count_rates);
        textView_view_count_movies = findViewById(R.id.textView_view_count_movies);
        textView_tab_1 = findViewById(R.id.textView_tab_1);
        textView_tab_2 = findViewById(R.id.textView_tab_2);
        textView_tab_3 = findViewById(R.id.textView_tab_3);
        gridlayout = findViewById(R.id.gridlayout);
        btn_trailer = findViewById(R.id.btn_trailer);
        btn_favourite = findViewById(R.id.btn_favourite);
        line_4 = findViewById(R.id.line_4);
        line_5 = findViewById(R.id.line_5);
        btn_show_more_review = findViewById(R.id.btn_show_more_review);
        btn_play = findViewById(R.id.btn_play);
        btn_back = findViewById(R.id.btn_back);
        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_3 = findViewById(R.id.tab_3);
        star_1 = findViewById(R.id.star_1);
        star_2 = findViewById(R.id.star_2);
        star_3 = findViewById(R.id.star_3);
        star_4 = findViewById(R.id.star_4);
        star_5 = findViewById(R.id.star_5);
        imageView_poser_3x4 = findViewById(R.id.imageView_poser_3x4);
        imageView_poser_4x3 = findViewById(R.id.imageView_poser_4x3);
    }

    private void btnFavouriteOnClick() {
        btn_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOGS" , hasLiked + "");

                MovieService movieService = ApiService.createService(MovieService.class);
                if (hasLiked == true) {
                    Call<ApiResponse> call = movieService.removeFavorite(movieId);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == Status.SUCCESS) {
                                    Toast.makeText(MoviesInformationActivity.this, "Xoá yêu thích thành công", Toast.LENGTH_SHORT).show();
                                    disliked();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });

                    return;
                } else {
                    Call<ApiResponse> call = movieService.addFavorite(movieId);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == Status.SUCCESS) {
                                    Toast.makeText(MoviesInformationActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                                    liked();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    public void btnTrailerOnClick(String url) {
        btn_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                PackageManager packageManager = MoviesInformationActivity.this.getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MoviesInformationActivity.this, "Ứng dụng YouTube chưa được cài đặt.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void liked() {
        hasLiked = true;
        btn_favourite.setBackgroundResource(R.drawable.ic_round_favorite_30);

    }

    private void disliked() {
        hasLiked= false;
        btn_favourite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_30);

    }

    private void setValueMovies() {
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();


        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<Movie>> call = movieService.getMovie(movieId);

        call.enqueue(new Callback<ApiResponse<Movie>>() {
            @Override
            public void onResponse(Call<ApiResponse<Movie>> call, Response<ApiResponse<Movie>> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body().getData();
                    textView_name_movies.setText(movie.getTitle());
                    textView_cast_movies.setText(movie.getActors());
                    textView_year_movies.setText(movie.getReleaseYear());
                    textView_director_movies.setText(movie.getDirector());
                    textView_time_movies.setText(movie.getDuration() + " phút");
                    Glide.with(MoviesInformationActivity.this).load(movie.getPosterVertical()).into(imageView_poser_3x4);
                    Glide.with(MoviesInformationActivity.this).load(movie.getPosterHorizontal()).into(imageView_poser_4x3);
                    textView_review.setText(movie.getDescription());
                    textView_category_movies.setText(movie.getGenre());
                    textView_nation_movies.setText(movie.getCountry());
                    textView_view_count_movies.setText(movie.getViewCounts() + "");

                    textView_count_rates.setText(movie.getNumberOfReviews() + " đánh giá");
                    hasLiked = movie.getHasFavorite();
                    if (movie.getHasFavorite() == true) {
                        liked();
                    }

                    name = movie.getTitle();
                    year = movie.getReleaseYear();

                    btnTrailerOnClick(movie.getTrailerURL());

                    List<String> episodes = movie.getVideoURL();
                    if (!episodes.isEmpty()) {
                        String firstEpisode = episodes.get(0);
                        btnPlayOnclick(firstEpisode );
                    }

                    for (int i = 0; i < episodes.size(); i++){
                        String split = episodes.get(i);
                        setLayoutEpisode("Tập " + (i+1), split);
                    }

                    try {
                        float AVERRAGE = movie.getRating();
                        if (AVERRAGE > 4) {
                            setStar5();
                        } else {
                            if (AVERRAGE > 3) {
                                setStar4();
                            } else {
                                if (AVERRAGE > 2) {
                                    setStar3();
                                } else {
                                    if (AVERRAGE > 1) {
                                        setStar2();
                                    } else {
                                        if (AVERRAGE == 0) {
                                            setStar0();
                                        } else {
                                            setStar1();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ArithmeticException e) {
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Movie>> call, Throwable t) {

            }
        });
    }


    private void btnBackOnClick() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void btnPlayOnclick(String value) {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MovieService movieService = ApiService.createService(MovieService.class);
                Call<ApiResponse> call = movieService.addViewCount(movieId);

                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {

                    }
                });



                Intent i1 = new Intent(MoviesInformationActivity.this, WatchMoviesActivity.class);
                i1.putExtra("link", value);
                i1.putExtra("name", name);
                i1.putExtra("year", year);
                startActivity(i1);
            }
        });
    }

    private void changeTab() {
        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_4.setBackgroundResource(R.color.red_40);
                line_5.setBackgroundResource(0);
                textView_tab_1.setTextColor(getResources().getColor(R.color.red));
                textView_tab_2.setTextColor(getResources().getColor(R.color.white));
                textView_tab_3.setTextColor(getResources().getColor(R.color.white));
                //replaceFragment(new RateStarFragment());

                RateStarFragment rateStarFragment = new RateStarFragment();
                replaceFragment(rateStarFragment, "id", movieId.toString());
            }
        });

        tab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_4.setBackgroundResource(R.color.red_40);
                line_5.setBackgroundResource(R.color.red_40);
                textView_tab_1.setTextColor(getResources().getColor(R.color.white));
                textView_tab_2.setTextColor(getResources().getColor(R.color.red));
                textView_tab_3.setTextColor(getResources().getColor(R.color.white));

                CommentsFragment commentsFragment = new CommentsFragment();
                replaceFragment(commentsFragment, "id", movieId.toString());
            }
        });

        tab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_4.setBackgroundResource(0);
                line_5.setBackgroundResource(R.color.red_40);
                textView_tab_1.setTextColor(getResources().getColor(R.color.white));
                textView_tab_2.setTextColor(getResources().getColor(R.color.white));
                textView_tab_3.setTextColor(getResources().getColor(R.color.red));
                RecommendFragment recommendFragment = new RecommendFragment();
                replaceFragment(recommendFragment, "id", movieId.toString());
            }
        });
    }

    private void btnShowHideReviewOnClick() {
        btn_show_more_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkSH) {
                    textView_review.setMaxLines(Integer.MAX_VALUE);
                    btn_show_more_review.setText("ẩn bớt.");
                    checkSH = true;
                } else {
                    textView_review.setMaxLines(3);
                    btn_show_more_review.setText("xem thêm...");
                    checkSH = false;
                }
            }
        });
    }

    private void setLayoutEpisode(String episodeName, String episodeLink) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dpToPx(45));
        params.setMargins(dpToPx(5), 0, dpToPx(5), 0);
        Button button = new Button(this);
        button.setLayoutParams(params);
        button.setText(episodeName);
        button.setBackgroundResource(R.color.red_40);
        button.setAllCaps(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoviesInformationActivity.this, WatchMoviesActivity.class);
                intent.putExtra("link", episodeLink);
                intent.putExtra("name", name);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });

        gridlayout.addView(button);
    }



    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void replaceFragment(Fragment fragment, String key, String value) {
        Bundle args = new Bundle();
        args.putString(key, value);
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    private void setStar5() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_24);
    }

    private void setStar4() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar3() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar2() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar1() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

    private void setStar0() {
        star_1.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_2.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_3.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_4.setBackgroundResource(R.drawable.ic_round_star_border_24);
        star_5.setBackgroundResource(R.drawable.ic_round_star_border_24);
    }

}
