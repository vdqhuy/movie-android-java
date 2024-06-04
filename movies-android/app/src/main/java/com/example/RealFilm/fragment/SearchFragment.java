package com.example.RealFilm.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.RealFilm.activity.MoviesInformationActivity;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private SearchView text_search;
    private GridLayout show_layout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        text_search = view.findViewById(R.id.text_search);
        show_layout = view.findViewById(R.id.show_layout);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        callApi();
        return view;
    }

    private void callApi() {
        text_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!text_search.getQuery().toString().isEmpty()) {

                    // Clear the current views in the GridLayout.
                    show_layout.removeAllViewsInLayout();

                    // Show loading
                    progressBar.setVisibility(View.VISIBLE);


                    MovieService movieService = ApiService.createService(MovieService.class);
                    Call<ApiResponse<List<Movie>>> call = movieService.searchMovie(query);
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void noResult() {
        progressBar.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
        params2.setMargins(0, dpToPx(10), 0, 0);
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(params2);
        textView.setText("Không tìm thấy kết quả!");
        textView.setTextSize(18);
        show_layout.addView(textView);
    }


    private void mappingMovie(List<Movie> movies) {
        for (Movie movie : movies) {
            String movieName = movie.getTitle();
            String posterUrl = movie.getPosterHorizontal();

            LinearLayout parent = new LinearLayout(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), 0);
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