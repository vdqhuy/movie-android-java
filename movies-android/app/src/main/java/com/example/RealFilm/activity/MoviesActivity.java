package com.example.RealFilm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.MovieService;
import com.example.RealFilm.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {

    private TextView textView_top;
    private GridLayout show_layout;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private ImageButton btn_back;
    private Map singleUser;
    private String idMovies;

    private String headerTitle;
    private String idIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        textView_top = findViewById(R.id.textView_top);
        show_layout = findViewById(R.id.show_layout);
        progressBar = findViewById(R.id.progressBar);
        btn_back = findViewById(R.id.btn_back);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();

        headerTitle= intent.getStringExtra("headerTitle");
        idIntent= intent.getStringExtra("id");

        btnBackOnClick();
        // ...rest call api
        getMovies();

    }



    private void getMovies(){
        Intent intent = getIntent();
        String str = intent.getStringExtra("key");

        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<List<Movie>>> call = null;

        switch (str){
            case Constants.MOVIE_LATEST:
                call = movieService.getMoviesLatest();
                textView_top.setText("Phim mới");
                break;
            case Constants.MOVIE_GENRE:
                call = movieService.getMoviesByGenre(idIntent);
                textView_top.setText(headerTitle);
                break;
            case Constants.MOVIE_COUNTRY:
                call = movieService.getMoviesByCountry(idIntent);
                textView_top.setText(headerTitle);
                break;
            case Constants.MOVIE_FAVORITE:
                call = movieService.getFavoriteMovies();
                textView_top.setText("Danh sách phim yêu thích");
                break;
            case Constants.MOVIE_COMMENT:
                call = movieService.getCommentedMovies();
                textView_top.setText("Danh sách phim đã bình luận");
                break;
            case Constants.MOVIE_STAR:
                call = movieService.getRatedMovies();
                textView_top.setText("Danh sách phim đã đánh giá");
                break;
        }

        if (call != null) {
            call.enqueue(new Callback<ApiResponse<List<Movie>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body().getData();
                        if (movies != null && !movies.isEmpty()) {
                            mappingMovie(movies);
                        } else {
                            noResult();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {

                }
            });
        }
    }

    private void noResult() {
        progressBar.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
        params2.setMargins(0, dpToPx(10), 0, 0);
        TextView textView = new TextView(this);
        textView.setLayoutParams(params2);
        textView.setText("Không tìm thấy kết quả!");
        textView.setTextSize(18);
        show_layout.addView(textView);
    }

    private void btnBackOnClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }




    private void mappingMovie(List<Movie> movies){
       for(Movie movie: movies){
           String movieName = movie.getTitle();
           String posterUrl = movie.getPosterVertical();

           LinearLayout parent = new LinearLayout(this);
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           params.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), 0);
           parent.setLayoutParams(params);
           parent.setOrientation(LinearLayout.VERTICAL);
           parent.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(MoviesActivity.this , MoviesInformationActivity.class);
                   intent.putExtra("id", movie.getId());
                   startActivity(intent);
               }
           });

           CardView cardView1 = new CardView(this);
           cardView1.setLayoutParams(new CardView.LayoutParams(dpToPx(162), dpToPx(217)));
           cardView1.setRadius(dpToPx(15));
           cardView1.setCardBackgroundColor(getResources().getColor(R.color.white));

           CardView.LayoutParams params3 = new CardView.LayoutParams(dpToPx(157), dpToPx(212), Gravity.CENTER);
           CardView cardView2 = new CardView(this);
           cardView2.setLayoutParams(params3);
           cardView2.setRadius(dpToPx(15));

           ImageView imageView = new ImageView(this);
           Glide.with(getApplicationContext()).load(posterUrl).into(imageView);
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
           imageView.setBackgroundResource(R.drawable.null_image34);

           LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
           params2.setMargins(0, dpToPx(10), 0, 0);
           TextView textView = new TextView(this);
           textView.setLayoutParams(params2);
           textView.setText(movieName);
           textView.setTextSize(18);
           textView.setMaxLines(2);
           textView.setEllipsize(TextUtils.TruncateAt.END);
           textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
           textView.setTextColor(getResources().getColor(R.color.white));

           show_layout.addView(parent);
           parent.addView(cardView1);
           cardView1.addView(cardView2);
           cardView2.addView(imageView);
           parent.addView(textView);
       }
        progressBar.setVisibility(View.INVISIBLE);
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}