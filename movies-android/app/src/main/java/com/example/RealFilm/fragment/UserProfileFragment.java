package com.example.RealFilm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.RealFilm.activity.ChangeInformationActivity;
import com.example.RealFilm.activity.LoginActivity;
import com.example.RealFilm.activity.MoviesActivity;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.User;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.UserService;
import com.example.RealFilm.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileFragment extends Fragment {

    private ProgressDialog progressDialog;
    private Button Btn_logout;
    private CardView list_favourite, list_comments, list_stars;
    private TextView Tv_user_name, Tv_user_id, Tv_user_email, Tv_user_birthday, Tv_user_joindate;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userId, email, name, birthday, joindate;
    private ImageView Btn_change_information, imageView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initUi(view);
        getUserInfor();
        onClickLogoutBtn();
        btnChangeInformationOnclick();
        btnListFavouriteOnClick();
        btnListCommentsOnClick();
        btnListStarsOnClick();
        return view;
    }
    private void initUi(View view){
        progressDialog = new ProgressDialog(getActivity());
        Btn_logout = view.findViewById(R.id.btn_logout);
        Btn_change_information = view.findViewById(R.id.btn_change_information);
        Tv_user_name = view.findViewById(R.id.texview_user_name);
        Tv_user_email = view.findViewById(R.id.texview_user_email);
        Tv_user_id = view.findViewById(R.id.texview_user_id);
        Tv_user_birthday = view.findViewById(R.id.texview_user_birthday);
        Tv_user_joindate = view.findViewById(R.id.texview_user_joindate);
        list_favourite = view.findViewById(R.id.list_favourite);
        list_comments = view.findViewById(R.id.list_comments);
        list_stars = view.findViewById(R.id.list_stars);
        imageView = view.findViewById(R.id.imageView);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = "HVuDpDfG91e8K1ZU7psuyRj3JzY2";
    }


    public void btnChangeInformationOnclick(){
        Btn_change_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getActivity(), ChangeInformationActivity.class);
                startActivity(i1);
            }
        });
    }

    private void btnListFavouriteOnClick(){
        list_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesActivity.class);
                intent.putExtra("key", Constants.MOVIE_FAVORITE);
                startActivity(intent);
            }
        });
    }
    private void btnListCommentsOnClick(){
        list_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesActivity.class);
                intent.putExtra("key", Constants.MOVIE_COMMENT);
                startActivity(intent);
            }
        });
    }

    private void btnListStarsOnClick(){
        list_stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , MoviesActivity.class);
                intent.putExtra("key", Constants.MOVIE_STAR);
                startActivity(intent);
            }
        });
    }

    private void getUserInfor(){
        progressDialog.setMessage("Đang xử lý...");
        progressDialog.show();

        UserService userService = ApiService.createService(UserService.class);
        Call<ApiResponse<User>> call = userService.getUser();
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if(response.isSuccessful()){
                    ApiResponse<User> res = response.body();
                    User user = res.getData();

                    name= user.getName();
                    userId = user.getId() + "";
                    email = user.getEmail();
                    birthday = user.getBirthday();
                    joindate= user.getCreatedAt().toString();
                    String avatar = user.getPhotoURL();
                    Glide.with(UserProfileFragment.this).load(avatar).into(imageView);

                    Tv_user_name.setText(name);
                    Tv_user_id.setText(userId);
                    Tv_user_email.setText(email);
                    Tv_user_birthday.setText(birthday);
                    Tv_user_joindate.setText(joindate);
                }
                progressDialog.dismiss();

            }
            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void onClickLogoutBtn(){
        Btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Đang đăng xuất, Vui lòng chờ...");
                progressDialog.show();
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(getActivity(), "Đăng xuất thành công!", Toast.LENGTH_LONG).show();
                Intent i1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(i1);
                progressDialog.dismiss();
                getActivity().finish();
            }
        });
    }

}