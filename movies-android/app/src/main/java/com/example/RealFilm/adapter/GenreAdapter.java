package com.example.RealFilm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.RealFilm.R;
import com.example.RealFilm.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private List<Genre> genres;
    private List<String> selectedGenres;

    public GenreAdapter(List<Genre> genres) {
        this.genres = genres;
        selectedGenres = new ArrayList<>();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkbox, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {

        Genre genre = genres.get(position);
        holder.checkBox.setText(genre.getName());
        holder.checkBox.setChecked(selectedGenres.contains(genre.getCode()));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedGenres.add(genre.getCode());
            } else {
                selectedGenres.remove(genre.getCode());
            }
        });
    }


    public void setSelectedGenres(List<String> selectedGenres) {
        this.selectedGenres.clear();
        this.selectedGenres.addAll(selectedGenres);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public List<String> getSelectedGenres() {
        return selectedGenres;
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}

