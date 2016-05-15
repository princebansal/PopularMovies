package com.nanodegree.android.popularmovies.Entity.Actors;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.nanodegree.android.popularmovies.Boundary.Database.MoviesDbHelper;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;



@Parcel
public class Movie {

    public static String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("poster_path")
    public String posterPath;
    public boolean adult;
    public String overview;
    @SerializedName("release_date")
    public String releaseDate;
    public int id;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("original_language")
    public String originalLanguage;
    public String title;
    @SerializedName("backdrop_path")
    public String backdropPath;
    public double popularity;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("vote_count")
    public int voteCount;
    public boolean video;
    @SerializedName("genre_ids")
    public List<Integer> genreIds;
    public boolean favourite=false;


    public String getPosterPath() {

        return POSTER_PATH_BASE_URL + posterPath;
    }

    public void setPosterPath(String posterPath) {

        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    // Create a Movie object from a cursor
    public static Movie fromCursor(Cursor curMovie) {

        Movie movie = new Movie();
        movie.setId(curMovie.getInt(curMovie.getColumnIndex(MoviesDbHelper.COL_ID)));
        movie.setPosterPath(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_POSTER_PATH)));
        movie.setAdult(Boolean.parseBoolean(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_ADULT))));
        movie.setOverview(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_OVERVIEW)));
        movie.setReleaseDate(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_RELEASE_DATE)));
        movie.setOriginalTitle(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_ORIGINAL_TITLE)));
        movie.setOriginalLanguage(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_ORIGINAL_LANGUAGE)));
        movie.setBackdropPath(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_BACKDROP_PATH)));
        movie.setPopularity(curMovie.getDouble(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_POPULARITY)));
        movie.setVoteAverage(curMovie.getDouble(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_VOTE_AVERAGE)));
        movie.setVoteCount(curMovie.getInt(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_VOTE_COUNT)));
        movie.setFavourite(Boolean.parseBoolean(curMovie.getString(curMovie.getColumnIndex(MoviesDbHelper.MOVIE_COL_FAVORITE))));

        return movie;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesDbHelper.COL_ID, id);
        values.put(MoviesDbHelper.MOVIE_COL_POSTER_PATH, posterPath);
        values.put(MoviesDbHelper.MOVIE_COL_ADULT, adult);
        values.put(MoviesDbHelper.MOVIE_COL_OVERVIEW, overview);
        values.put(MoviesDbHelper.MOVIE_COL_RELEASE_DATE, releaseDate);
        values.put(MoviesDbHelper.MOVIE_COL_ORIGINAL_TITLE, originalTitle);
        values.put(MoviesDbHelper.MOVIE_COL_ORIGINAL_LANGUAGE, originalLanguage);
        values.put(MoviesDbHelper.MOVIE_COL_BACKDROP_PATH, backdropPath);
        values.put(MoviesDbHelper.MOVIE_COL_POPULARITY, popularity);
        values.put(MoviesDbHelper.MOVIE_COL_VOTE_AVERAGE, voteAverage);
        values.put(MoviesDbHelper.MOVIE_COL_VOTE_COUNT, voteCount);
        values.put(MoviesDbHelper.MOVIE_COL_FAVORITE, favourite);
        return values;
    }

}
