package com.example.RealFilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.model.Comment;

import java.util.Calendar;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    Context context;
    List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_entry_cmt, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment cmt = commentList.get(position);
        if (cmt.getUser().getPhotoURL() != null) {
            Glide.with(holder.itemView)
                    .load(cmt.getUser().getPhotoURL())
                    .circleCrop()
                    .into(holder.imgAvatar);
        }
        else {
            Glide.with(holder.itemView).load(context.getResources().getDrawable(R.drawable.default_avatar))
                    .circleCrop()
                    .into(holder.imgAvatar);
        }
        holder.txtUsername.setText(cmt.getUser().getName());
        holder.txtContent.setText(cmt.getComment());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cmt.getUpdatedAt());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        holder.txtTime.setText(makeFullDateString(hour, minute, day, month, year));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtUsername, txtContent, txtTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtTime = itemView.findViewById(R.id.txt_time);
        }
    }

    private String makeFullDateString(int hour, int minute, int day, int month, int year) {
        return hour + ":" + minute + ", "
                + day + "/" + month + "/" + year;
    }
}
