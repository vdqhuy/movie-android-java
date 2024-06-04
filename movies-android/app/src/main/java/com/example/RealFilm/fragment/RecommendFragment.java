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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.RealFilm.activity.MoviesInformationActivity;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.MovieService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String category;
    private GridLayout show_layout;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private String moviesID;

    public RecommendFragment() {
        // Required empty public constructor
    }


    public static RecommendFragment newInstance(String param1, String param2) {
        RecommendFragment fragment = new RecommendFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        show_layout = view.findViewById(R.id.show_layout);
        progressBar = view.findViewById(R.id.progressBar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        moviesID = getArguments().getString("id", "");
        browserData();
        return view;
    }

    private void browserData(){
        MovieService movieService = ApiService.createService(MovieService.class);
        Call<ApiResponse<List<Movie>>> call = movieService.getMoviesRecommendation(Integer.valueOf(moviesID));

        call.enqueue(new Callback<ApiResponse<List<Movie>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Movie>>> call, Response<ApiResponse<List<Movie>>> response) {

                if(response.isSuccessful()){
                    List<Movie> movies = response.body().getData();
                    if (movies != null && !movies.isEmpty()) {
                        setLayoutSearch(movies);
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Movie>>> call, Throwable t) {

            }
        });

//        mDatabase.child("Movies").addListenerForSingleValueEvent( new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChildren()){
//
//                    category = getArguments().getString("category", "").trim();
//                    System.out.println("CATEGORY: " + category);
//                    String[] words= category.split(",");
//                    int random_int = (int)(Math.random() * (words.length));
//                    System.out.println("random int: " + random_int);
//                    System.out.println("random String: " + words[random_int]);
//                    collectData((Map<String,Object>) dataSnapshot.getValue(), words[random_int]);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

    }


    private void setLayoutSearch(List<Movie> movies){
       for(Movie movie: movies) {

           LinearLayout parent = new LinearLayout(getActivity());
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           params.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), 0);
           parent.setLayoutParams(params);
           parent.setOrientation(LinearLayout.VERTICAL);
           parent.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(getActivity() ,MoviesInformationActivity.class);
                   intent.putExtra("id",  movie.getId());
                   startActivity(intent);
               }
           });

           CardView cardView1 = new CardView(getActivity());
           cardView1.setLayoutParams(new CardView.LayoutParams(dpToPx(162), dpToPx(217)));
           cardView1.setRadius(dpToPx(15));
           cardView1.setCardBackgroundColor(getResources().getColor(R.color.white));

           CardView.LayoutParams params3 = new CardView.LayoutParams(dpToPx(157), dpToPx(212), Gravity.CENTER);
           CardView cardView2 = new CardView(getActivity());
           cardView2.setLayoutParams(params3);
           cardView2.setRadius(dpToPx(15));

           ImageView imageView = new ImageView(getActivity());
           Glide.with(getActivity()).load(movie.getPosterVertical()).into(imageView);
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
           imageView.setBackgroundResource(R.drawable.null_image34);

           LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(45));
           params2.setMargins(0, dpToPx(10), 0, 0);
           TextView textView = new TextView(getActivity());
           textView.setLayoutParams(params2);
           textView.setText(movie.getTitle());
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