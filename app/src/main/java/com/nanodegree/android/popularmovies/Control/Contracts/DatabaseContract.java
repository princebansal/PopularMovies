package com.nanodegree.android.popularmovies.Control.Contracts;

import android.net.Uri;


public class DatabaseContract {


    //Content Types
    public static final String CONTENT_ITEM_TYPE_MOVIE = "popularmovies.android.cursor.item/popularmovies.movie";
    public static final String CONTENT_ITEM_TYPE_REVIEW = "popularmovies.android.cursor.item/popularmovies.review";
    public static final String CONTENT_ITEM_TYPE_VIDEO = "popularmovies.android.cursor.item/popularmovies.video";
    public static final String CONTENT_TYPE_DIR_MOVIE = "popularmovies.android.cursor.dir/popularmovies.movie";
    public static final String CONTENT_TYPE_DIR_REVIEW = "popularmovies.android.cursor.dir/popularmovies.review";
    public static final String CONTENT_TYPE_DIR_VIDEO = "popularmovies.android.cursor.dir/popularmovies.video";

    public static final String AUTHORITY = "popularmovies.android.nanodegree.provider";
    // content://<authority>/<path to type>
    public static final Uri CONTENT_URI_MOVIE = Uri.parse("content://" + AUTHORITY + "/movies");
    public static final Uri CONTENT_URI_REVIEW = Uri.parse("content://" + AUTHORITY + "/reviews");
    public static final Uri CONTENT_URI_VIDEO = Uri.parse("content://" + AUTHORITY + "/videos");


}
