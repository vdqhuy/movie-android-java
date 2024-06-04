package com.example.RealFilm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("director")
    private String director;

    @SerializedName("releaseYear")
    private String releaseYear;

    @SerializedName("duration")
    private String duration;

    @SerializedName("posterHorizontal")
    private String posterHorizontal;

    @SerializedName("posterVertical")
    private String posterVertical;

    @SerializedName("actors")
    private String actors;

    @SerializedName("trailerURL")
    private String trailerURL;

    @SerializedName("genre")
    private String genre;

    @SerializedName("country")
    private String country;

    @SerializedName("hasFavorite")
    private Boolean hasFavorite;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("numberOfReviews")
    private Integer numberOfReviews;

    @SerializedName("videoURL")
    private List<String> videoURL;

    @SerializedName("viewCounts")
    private Integer viewCounts;

    public List<String> getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(List<String> videoURL) {
        this.videoURL = videoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPosterHorizontal() {
        return posterHorizontal;
    }

    public void setPosterHorizontal(String posterHorizontal) {
        this.posterHorizontal = posterHorizontal;
    }

    public String getPosterVertical() {
        return posterVertical;
    }

    public void setPosterVertical(String posterVertical) {
        this.posterVertical = posterVertical;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(Integer numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public Integer getViewCounts() {
        return viewCounts;
    }

    public void setViewCounts(Integer viewCounts) {
        this.viewCounts = viewCounts;
    }

    public void Movie () {}
}
