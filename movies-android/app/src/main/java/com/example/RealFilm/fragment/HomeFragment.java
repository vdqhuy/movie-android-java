package com.example.RealFilm.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.activity.MoviesActivity;
import com.example.RealFilm.activity.MoviesInformationActivity;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Country;
import com.example.RealFilm.model.Genre;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.CommonService;
import com.example.RealFilm.service.MovieService;
import com.example.RealFilm.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private TextView textView_category, textView_new_movies, textView_nation, textView_category_movies_home, textView_name_movies_home;
    private Button btn_play;
    private ImageView imageView_poster_home_3x4;
    private DatabaseReference mDatabase;
    private LinearLayout  btn_information_home, btn_trailer_home;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;

    private List<Genre> genres;
    private List<Country> countries;
    private List<Movie> moviesLatest;

    private LinearLayout movieContainer;

    private String defaultGenre;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        textView_category_movies_home = rootView.findViewById(R.id.textView_category_movies_home);
        textView_name_movies_home = rootView.findViewById(R.id.textView_name_movies_home);
        textView_new_movies = rootView.findViewById(R.id.textView_new_movies);
        imageView_poster_home_3x4 = rootView.findViewById(R.id.imageView_poster_home_3x4);

        btn_information_home = rootView.findViewById(R.id.btn_information_home);
        btn_trailer_home = rootView.findViewById(R.id.btn_trailer_home);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        btn_play = rootView.findViewById(R.id.btn_play);
        textView_category = rootView.findViewById(R.id.textView_category);
        textView_nation = rootView.findViewById(R.id.textView_nation);
        movieContainer = rootView.findViewById(R.id.movieContainer);

        nemuCategoryOnClick();


        // ...rest call api
        getCountries();
        getGenres();
        getMoviesLatest();

        return rootView;
    }

    private  void btnTrailerOnClick(String trailer) {
        btn_trailer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(trailer);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");

                PackageManager packageManager = getActivity().getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Ứng dụng YouTube chưa được cài đặt.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  getMoviesLatest () {
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<List<Movie>>> call = movieService.getMoviesLatest();
        call.enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getData();
                    if (movies != null && !movies.isEmpty()) {
                        Object[] movieArray = movies.toArray();
                        moviesLatest= movies;
                        int randomIndex = (int)(Math.random() * (movieArray.length));
                        Movie randomMovie = (Movie) movieArray[randomIndex];
                        String posterUrl = randomMovie.getPosterHorizontal();

                        Glide.with(getActivity()).load(posterUrl).into(imageView_poster_home_3x4);
                        textView_name_movies_home.setText(randomMovie.getTitle());
                        defaultGenre = (randomMovie.getGenre());
                        btnTrailerOnClick(randomMovie.getTrailerURL());
                        btnPlayOnClick(randomMovie.getId());
                        setDefaultGenreValue();

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {

            }
        });
    }


    public void getMovieListBySelectedGenre(String genreCode) {
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<List<Movie>>> call = movieService.getMoviesByGenre(genreCode);
        call.enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getData();
                    if (movies != null && !movies.isEmpty()) {
                        showMoviesByGenre(genreCode, movies);
                    }

                } else {
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {

            }
        });
    }

    private void showMoviesByGenre(String genreCode, List<Movie> movies) {
        View genreView = movieContainer.findViewWithTag(genreCode);
        if (genreView != null) {
            ProgressBar genreLoadingProgressBar = genreView.findViewById(R.id.genreLoadingProgressBar);
            HorizontalScrollView genreMoviesScrollView = genreView.findViewById(R.id.genreMoviesScrollView);
            LinearLayout genreMoviesLayout = genreView.findViewById(R.id.genreMoviesLayout);
            genreLoadingProgressBar.setVisibility(View.GONE);
            genreMoviesScrollView.setVisibility(View.VISIBLE);

            for (Movie movie : movies) {
                String movieName = movie.getTitle();
                String posterUrl = movie.getPosterVertical();
                LinearLayout parent = new LinearLayout(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, dpToPx(10), dpToPx(20), 0);
                parent.setLayoutParams(params);
                parent.setOrientation(LinearLayout.VERTICAL);
                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MoviesInformationActivity.class);
                        intent.putExtra("id", movie.getId());
                        startActivity(intent);
                    }
                });

                CardView cardView1 = new CardView(getActivity());
                cardView1.setLayoutParams(new CardView.LayoutParams(dpToPx(165), dpToPx(220)));
                cardView1.setRadius(dpToPx(15));
                cardView1.setCardBackgroundColor(getResources().getColor(R.color.white));

                CardView.LayoutParams params3 = new CardView.LayoutParams(dpToPx(160), dpToPx(215), Gravity.CENTER);
                CardView cardView2 = new CardView(getActivity());
                cardView2.setLayoutParams(params3);
                cardView2.setRadius(dpToPx(15));

                ImageView imageView = new ImageView(getActivity());
                imageView.setId((int) 1);
                Glide.with(getActivity()).load(posterUrl).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imageView.setBackgroundResource(R.drawable.null_image34);

                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
                params2.setMargins(0, dpToPx(10), 0, 0);
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(params2);
                textView.setText(movieName);
                textView.setTextSize(18);
                textView.setMaxLines(2);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setTextColor(getResources().getColor(R.color.white));


                parent.addView(cardView1);
                cardView1.addView(cardView2);
                cardView2.addView(imageView);
                parent.addView(textView);

                genreMoviesLayout.addView(parent);
            }
        }

    }

    private void getCountries() {
        CommonService commonService = ApiService.createService(CommonService.class);
        Call<ApiResponse<List<Country>>> call = commonService.getCountries();

        call.enqueue(new Callback<ApiResponse<List<Country>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Country>>> call, Response<ApiResponse<List<Country>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Country>> res = response.body();
                    countries = res.getData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Country>>> call, Throwable t) {

            }
        });

    }

    private void setDefaultGenreValue () {
        if(defaultGenre != null && genres !=null) {

            List<String> selectedGenres = new ArrayList<>();
            for (String genreCode : defaultGenre.split(",")) {
                selectedGenres.add(genreCode.trim());
            }
            List<String> genreNames = new ArrayList<>();
            for (String selected : selectedGenres) {
                for (Genre genre : genres) {
                    if (selected.equals(genre.getCode())) {
                        genreNames.add(genre.getName());
                        break;

                    }
                }

            }

            StringBuilder genresStringBuilder = new StringBuilder();
            for (int i = 0; i < genreNames.size(); i++) {
                genresStringBuilder.append(genreNames.get(i));
                if (i < genreNames.size() - 1) {
                    genresStringBuilder.append(", ");
                }
            }

            String genresString = genresStringBuilder.toString();
            textView_category_movies_home.setText(genresString);

        }

    }

    private void getGenres() {
        CommonService commonService = ApiService.createService(CommonService.class);
        Call<ApiResponse<List<Genre>>> call = commonService.getGenres();

        call.enqueue(new Callback<ApiResponse<List<Genre>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<Genre>>> call, Response<ApiResponse<List<Genre>>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<List<Genre>> res = response.body();
                    System.out.println(res.getAccessToken());
                    genres = res.getData();
                    for (Genre genre : genres) {
                        addGenreView(genre.getName(), genre.getCode());
                        getMovieListBySelectedGenre(genre.getCode());
                    }
                    setDefaultGenreValue();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Genre>>> call, Throwable t) {

            }
        });
    }

    private void addGenreView(String genreName, String genreCode) {
        View genreView = LayoutInflater.from(getActivity()).inflate(R.layout.item_category, null);
        genreView.setTag(genreCode);

        TextView genreNameTextView = genreView.findViewById(R.id.genreNameTextView);
        TextView btn_more = genreView.findViewById(R.id.btn_more_recommend_movies);
        HorizontalScrollView genreMoviesScrollView = genreView.findViewById(R.id.genreMoviesScrollView);
        genreNameTextView.setText(genreName);
        genreMoviesScrollView.setVisibility(View.GONE);

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(Constants.MOVIE_GENRE, genreCode, genreName);
            }
        });

        movieContainer.addView(genreView);
    }


    private void sendData(String value, String id, String headerTitle) {
        Intent intent = new Intent(getActivity(), MoviesActivity.class);
        intent.putExtra("key", value);
        intent.putExtra("id", id);
        intent.putExtra("headerTitle", headerTitle);
        startActivity(intent);
    }

    private void nemuCategoryOnClick() {
        textView_new_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(Constants.MOVIE_LATEST, null, null);
            }
        });

        textView_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(getActivity(), v);

                for (Genre genre : genres) {
                    popupMenu.getMenu().add(Menu.NONE, Menu.NONE, genres.indexOf(genre), genre.getName());
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    int position = item.getOrder();
                    Genre selectedGenre = genres.get(position);
                    String code = selectedGenre.getCode();
                    String name = selectedGenre.getName();
                    sendData(Constants.MOVIE_GENRE, code, name);
                    return true;
                });

                popupMenu.show();

            }
        });

        textView_nation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);

                for (Country country : countries) {
                    popupMenu.getMenu().add(Menu.NONE, Menu.NONE, countries.indexOf(country), country.getName());
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    int position = item.getOrder();
                    Country selectedCountry = countries.get(position);
                    String code = selectedCountry.getCode();
                    String name = selectedCountry.getName();
                    sendData(Constants.MOVIE_COUNTRY, code, name);
                    return true;
                });

                popupMenu.show();
            }
        });
    }




    private void btnPlayOnClick(Integer id) {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoviesInformationActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        btn_information_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoviesInformationActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }


    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}