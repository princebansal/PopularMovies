package com.nanodegree.android.popularmovies.Entity.Actors;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.nanodegree.android.popularmovies.Boundary.Database.MoviesDbHelper;

/**
 * Created by Prince Bansal Local on 14-05-2016.
 */
public class Video {

    public static final String VIDEO_URL_YOUTUBE_PREFIX="http://www.youtube.com/watch?v=";

    public String id;
    @SerializedName("iso_639_1")
    public String iso6391;
    @SerializedName("iso_3166_1")
    public String iso31661;
    public String key;
    public String name;
    public String site;
    public String type;
    public int size;
    public int movieId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    // Create a Video object from a cursor
    public static Video fromCursor(Cursor curVideo) {
        Video video = new Video();
        video.setId(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.COL_ID)));
        video.setIso6391(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_ISO_639_1)));
        video.setIso31661(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_ISO_3166_1)));
        video.setKey(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_KEY)));
        video.setName(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_NAME)));
        video.setSite(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_SITE)));
        video.setType(curVideo.getString(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_TYPE)));
        video.setSize(curVideo.getInt(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_SIZE)));
        video.setMovieId(curVideo.getInt(curVideo.getColumnIndex(MoviesDbHelper.VIDEO_COL_MOVIE_ID)));
        return video;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesDbHelper.COL_ID, id);
        values.put(MoviesDbHelper.VIDEO_COL_ISO_639_1,iso6391 );
        values.put(MoviesDbHelper.VIDEO_COL_ISO_3166_1, iso31661);
        values.put(MoviesDbHelper.VIDEO_COL_KEY, key);
        values.put(MoviesDbHelper.VIDEO_COL_NAME, name);
        values.put(MoviesDbHelper.VIDEO_COL_SITE, site);
        values.put(MoviesDbHelper.VIDEO_COL_TYPE, type);
        values.put(MoviesDbHelper.VIDEO_COL_SIZE, size);
        values.put(MoviesDbHelper.VIDEO_COL_MOVIE_ID, movieId);
        return values;
    }
}
