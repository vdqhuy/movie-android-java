package com.example.RealFilm.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.RealFilm.R;
import com.example.RealFilm.activity.EditMovieActivity;
import com.example.RealFilm.listerner.MovieDeleteListener;
import com.example.RealFilm.model.Movie;

import java.util.List;

public class ManagerMovieAdapter extends RecyclerView.Adapter<ManagerMovieAdapter.MovieViewHolder> {
    // ...
    private List<Movie> movies;
    private MovieDeleteListener movieDeleteListener;

    // Constructor
    public ManagerMovieAdapter(List<Movie> movies, MovieDeleteListener listener) {
        this.movies = movies;
        this.movieDeleteListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_manager, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.textMovieTitle.setText(movie.getTitle());
        Glide.with(holder.itemView)
                .load(movie.getPosterHorizontal())
                .into(holder.imageMovie);

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    // Confirm dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Xóa phim");
                    builder.setMessage("Bạn có chắc chắn muốn xóa phim này?");

                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMovie(position);
                        }
                    });

                    builder.setNegativeButton("Hủy", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = movies.get(holder.getAdapterPosition());
                int movieId = movie.getId();

                Intent intent = new Intent(v.getContext(), EditMovieActivity.class);
                intent.putExtra("movieId", movieId);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    // Method to delete a movie.
    public void deleteMovie(int position) {
        Movie movie = movies.get(position);
        //  Call the "onDeleteMovie" method of the listener.
        if (movieDeleteListener != null) {
            movieDeleteListener.onDeleteMovie(movie.getId());
        }
    }


    static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imageMovie;
        TextView textMovieTitle;
        ImageView buttonEdit;
        ImageView buttonDelete;

        MovieViewHolder(View itemView) {
            super(itemView);
            // ... mapping
            imageMovie = itemView.findViewById(R.id.image_movie);
            textMovieTitle = itemView.findViewById(R.id.text_movie_title);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
