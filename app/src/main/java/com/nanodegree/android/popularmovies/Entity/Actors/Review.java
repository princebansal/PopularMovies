package com.nanodegree.android.popularmovies.Entity.Actors;

import android.content.ContentValues;
import android.database.Cursor;

import com.nanodegree.android.popularmovies.Boundary.Database.MoviesDbHelper;

/**
 * Created by Prince Bansal Local on 14-05-2016.
 */
public class Review {

    public String id;
    public String author;
    public String content;
    public String url;
    public int movieId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    // Create a Review object from a cursor
    public static Review fromCursor(Cursor curReview) {
        Review review = new Review();
        review.setId(curReview.getString(curReview.getColumnIndex(MoviesDbHelper.COL_ID)));
        review.setAuthor(curReview.getString(curReview.getColumnIndex(MoviesDbHelper.REVIEW_COL_AUTHOR)));
        review.setContent(curReview.getString(curReview.getColumnIndex(MoviesDbHelper.REVIEW_COL_CONTENT)));
        review.setUrl(curReview.getString(curReview.getColumnIndex(MoviesDbHelper.REVIEW_COL_URL)));
        review.setMovieId(curReview.getInt(curReview.getColumnIndex(MoviesDbHelper.REVIEW_COL_MOVIE_ID)));
        return review;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesDbHelper.COL_ID, id);
        values.put(MoviesDbHelper.REVIEW_COL_AUTHOR, author);
        values.put(MoviesDbHelper.REVIEW_COL_CONTENT, content);
        values.put(MoviesDbHelper.REVIEW_COL_URL, url);
        values.put(MoviesDbHelper.REVIEW_COL_MOVIE_ID, movieId);
        return values;
    }

}
