package com.example.RealFilm.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.model.ApiResponse;
import com.example.RealFilm.model.Comment;
import com.example.RealFilm.model.Status;
import com.example.RealFilm.service.ApiService;
import com.example.RealFilm.service.CommentService;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText editText_comment;
    private LinearLayout layout_set_comments;
    private TextView textView_comments_count;
    private Button btn_send_comment;
    private String moviesID, userID;


    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;

    public CommentsFragment() {
    }

    public static CommentsFragment newInstance(String param1, String param2) {
        CommentsFragment fragment = new CommentsFragment();
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
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        moviesID = getArguments().getString("id", "");
        initUi(view);
        showComments();
        btnSendCommentOnClick();
        return view;
    }

    private void initUi(View view){
        editText_comment = view.findViewById(R.id.editText_comment);
        layout_set_comments = view.findViewById(R.id.layout_set_comments);
        btn_send_comment = view.findViewById(R.id.btn_send_comment);
        textView_comments_count = view.findViewById(R.id.textView_comments_count);

    }

    private void showComments(){

        CommentService commentService = ApiService.createService(CommentService.class);
        Call<ApiResponse<List<Comment>>> call = commentService.getCommentByMovie(Integer.valueOf(moviesID));

        call.enqueue(new Callback<ApiResponse<List<Comment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comment>>> call, Response<ApiResponse<List<Comment>>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body().getData();
                    if (comments != null && !comments.isEmpty()) {
                        collectComments(comments);
                    } else {
                        textView_comments_count.setText("0 bình luận");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comment>>> call, Throwable t) {

            }
        });

    }

    private void collectComments(List<Comment> comments) {
        int COMMENT_COUNT = 0;
        for(Comment comment: comments){
            COMMENT_COUNT++;
            String name = comment.getUser().getName();
            String time_= comment.getCreatedAt();
            String comment_ = comment.getComment();
            String avatar =comment.getUser().getPhotoURL();
            setLayoutComment(name, time_, comment_, avatar);
            textView_comments_count.setText(COMMENT_COUNT + " bình luận");

        }

    }

    private void setLayoutComment(String userid, String time, String comment, String avatar_link){
        LinearLayout parent = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins( 0, 0, 0, 0);
        parent.setGravity(Gravity.CENTER);
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout parent3 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params6.setMargins(dpToPx(10), 0, dpToPx(10), dpToPx(20));
        parent3.setLayoutParams(params6);
        parent3.setOrientation(LinearLayout.VERTICAL);


        LinearLayout parent2 = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins( dpToPx(10), 0,0, 0);
        parent2.setLayoutParams(params2);
        parent2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout2 = new LinearLayout(getActivity());
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.HORIZONTAL);


        TextView USER_NAME = new TextView(getActivity());
        TextView COMMENT_TIME = new TextView(getActivity());
        TextView COMMENT = new TextView(getActivity());
        View view = new View(getActivity());

        USER_NAME.setText(userid);
        USER_NAME.setTextSize(16);
        USER_NAME.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.baloo));
        USER_NAME.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        COMMENT_TIME.setText(time);

        COMMENT.setText(comment);
        COMMENT.setTextColor(getResources().getColor(R.color.white));
        COMMENT.setTextSize(18);

        view.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3));
        view.setBackgroundResource(R.color.red);

        CardView.LayoutParams params3 = new CardView.LayoutParams(dpToPx(50), dpToPx(50), Gravity.CENTER);
        CardView cardView = new CardView(getActivity());
        cardView.setLayoutParams(params3);
        cardView.setRadius(dpToPx(15));

        ImageView imageView = new ImageView(getActivity());
        Glide.with(getActivity()).load(avatar_link).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        cardView.addView(imageView);

        layout2.addView(USER_NAME);
        layout2.addView(COMMENT_TIME);

        parent.addView(cardView);
        parent.addView(parent2);


        parent2.addView(layout2);
        parent2.addView(COMMENT);

        parent3.addView(parent);
        parent3.addView(view);

        layout_set_comments.addView(parent3);

    }

    private void btnSendCommentOnClick(){
        btn_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editText_comment.getText().toString().trim();
                userID = "dm";
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss\ndd/MM/yyyy");
                Date date = new Date();
                String time = dateFormat.format(date);
                if (!comment.isEmpty()){
                    addComment(comment);
                };
                reload();
            }
        });
    }

    private void reload(){

        editText_comment.setText("");
        layout_set_comments.removeAllViewsInLayout();
        showComments();
    }

    private void addComment(String comment){

        CommentService commentService = ApiService.createService(CommentService.class);
        Call<ApiResponse> call = commentService.createComment(Integer.valueOf(moviesID),comment);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().getStatus() == Status.SUCCESS){
                        Toast.makeText(getActivity(), "Đã gửi", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}