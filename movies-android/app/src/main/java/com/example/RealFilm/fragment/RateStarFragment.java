package com.example.RealFilm.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Movie;
import com.example.RealFilm.model.Rate;
import com.example.RealFilm.model.Status;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.MovieService;
import com.example.RealFilm.service.RatingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateStarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ImageView star_1, star_2, star_3, star_4, star_5;
    private Button btn_send_rate;
    private String moviesID;
    private int STAR_COUNT = 0;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private int STAR = 0, TOTAL_RATING = 0;

    public RateStarFragment() {
        // Required empty public constructor
    }


    public static RateStarFragment newInstance(String param1, String param2) {
        RateStarFragment fragment = new RateStarFragment();
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
        View view = inflater.inflate(R.layout.fragment_rate_star, container, false);

        star_1 = view.findViewById(R.id.star_1);
        star_2 = view.findViewById(R.id.star_2);
        star_3 = view.findViewById(R.id.star_3);
        star_4 = view.findViewById(R.id.star_4);
        star_5 = view.findViewById(R.id.star_5);
        btn_send_rate = view.findViewById(R.id.btn_send_rate);

        moviesID = getArguments().getString("id", "");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rateStar();
        sendRateOnclick();
        showStars();


        return view;
    }

    private void sendRateOnclick() {
        btn_send_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (STAR_COUNT == 0) {
                    Toast.makeText(getActivity(), "Vui lòng đánh giá trước khi gửi!", Toast.LENGTH_SHORT).show();
                } else {
                    send();
                }
            }
        });
    }

    private void showStars() {
        setStar0();
        RatingService ratingService = ApiService.createService(RatingService.class);
        Call<ApiResponse<List<Rate>>> call = ratingService.getMovieRate(Integer.valueOf(moviesID));

        call.enqueue(new Callback<ApiResponse<List<Rate>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Rate>>> call, Response<ApiResponse<List<Rate>>> response) {
                if (response.body().getStatus() == Status.SUCCESS) {
                    List<Rate> rates = response.body().getData();
                    int totalRates = 0;
                    if (rates != null) {
                        for (Rate rate : rates) {
                            if (rate != null) {
                                totalRates += rate.getRating();
                            }
                            else {
                                setStar0();
                            }
                        }
                        STAR_COUNT = totalRates/rates.size();
                    }
                    else STAR_COUNT = 0;

                    switch (STAR_COUNT) {
                        case 1:
                            setStar1();
                            break;
                        case 2:
                            setStar2();
                            break;
                        case 3:
                            setStar3();
                            break;
                        case 4:
                            setStar4();
                            break;
                        case 5:
                            setStar5();
                            break;
                        default:
                            setStar0();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Rate>>> call, Throwable t) {
                setStar0();
            }
        });
    }

    private void send() {
        Log.d("COUNT", STAR_COUNT + "");

        RatingService ratingService = ApiService.createService(RatingService.class);
        Call<ApiResponse> call = ratingService.rateMovie(Integer.valueOf(moviesID), STAR_COUNT);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getStatus() == Status.SUCCESS) {
                    Toast.makeText(getActivity(), "Đánh giá thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }

    private void rateStar() {
        star_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 1;
                setStar1();
            }
        });
        star_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 2;
                setStar2();
            }
        });
        star_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 3;
                setStar3();
            }
        });
        star_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 4;
                setStar4();
            }
        });
        star_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STAR_COUNT = 5;
                setStar5();
            }
        });
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


    public class stars {
        public int star, total_rating;

        public stars() {

        }

        public stars(int star, int total_rating) {
            this.star = star;
            this.total_rating = total_rating;
        }
    }
}