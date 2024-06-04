package com.example.RealFilm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.activity.ChartActivity;
import com.example.RealFilm.activity.ChartMovieDetailActivity;
import com.example.RealFilm.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    Context context;
    List<Movie> movieList;

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_film, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie mv = movieList.get(position);
        Glide.with(holder.itemView)
                .load(mv.getPosterHorizontal())
                .into(holder.imgMovie);
        holder.txtTitle.setText(mv.getTitle());

        holder.cardFilmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imgInfo.performClick();
            }
        });

        holder.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChartMovieDetailActivity.class);
                intent.putExtra("MovieId", String.valueOf(mv.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cardFilmInfo;
        TextView txtTitle;
        ImageView imgMovie, imgInfo;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFilmInfo = itemView.findViewById(R.id.card_film_info);
            imgMovie = itemView.findViewById(R.id.img_movie);
            txtTitle = itemView.findViewById(R.id.txt_title);
            imgInfo = itemView.findViewById(R.id.img_info);
        }
    }
}
