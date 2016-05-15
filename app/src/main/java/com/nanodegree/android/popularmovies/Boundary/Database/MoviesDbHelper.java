package com.nanodegree.android.popularmovies.Boundary.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_VIDEOS = "trailers";
    public static final String TABLE_REVIEWS = "reviews";

    //Common columns
    public static final String COL_ID = "_id";

    //Movie table columns
    public static final String MOVIE_COL_POSTER_PATH = "poster_path";
    public static final String MOVIE_COL_ADULT = "adult";
    public static final String MOVIE_COL_OVERVIEW = "overview";
    public static final String MOVIE_COL_RELEASE_DATE = "release_date";
    public static final String MOVIE_COL_ORIGINAL_TITLE = "original_title";
    public static final String MOVIE_COL_ORIGINAL_LANGUAGE = "original_language";
    public static final String MOVIE_COL_BACKDROP_PATH = "backdrop_path";
    public static final String MOVIE_COL_POPULARITY = "popularity";
    public static final String MOVIE_COL_VOTE_AVERAGE = "vote_average";
    public static final String MOVIE_COL_VOTE_COUNT = "vote_count";
    public static final String MOVIE_COL_FAVORITE = "favourite";

    //Reviews table columns
    public static final String REVIEW_COL_AUTHOR = "author";
    public static final String REVIEW_COL_CONTENT = "content";
    public static final String REVIEW_COL_URL = "url";
    public static final String REVIEW_COL_MOVIE_ID = "movie_id";


    //Reviews table columns
    public static final String VIDEO_COL_ISO_639_1 = "iso_639_1";
    public static final String VIDEO_COL_ISO_3166_1 = "iso_3166_1";
    public static final String VIDEO_COL_KEY = "key";
    public static final String VIDEO_COL_NAME = "name";
    public static final String VIDEO_COL_SITE = "site";
    public static final String VIDEO_COL_TYPE = "type";
    public static final String VIDEO_COL_SIZE = "size";
    public static final String VIDEO_COL_MOVIE_ID = "movie_id";


    //Create table statements
    public static final String CREATE_TABLE_MOVIES = "create table "
            + TABLE_MOVIES + "(" +
            COL_ID + " int   primary key, " +
            MOVIE_COL_POSTER_PATH + " text, " +
            MOVIE_COL_ADULT + " text, " +
            MOVIE_COL_OVERVIEW + " text, " +
            MOVIE_COL_RELEASE_DATE + " text, " +
            MOVIE_COL_ORIGINAL_TITLE + " text, " +
            MOVIE_COL_ORIGINAL_LANGUAGE + " text, " +
            MOVIE_COL_BACKDROP_PATH + " text, " +
            MOVIE_COL_POPULARITY + " double, " +
            MOVIE_COL_VOTE_AVERAGE + " double, " +
            MOVIE_COL_VOTE_COUNT + " int, " +
            MOVIE_COL_FAVORITE + " text " +
            ");";

    public static final String CREATE_TABLE_REVIEWS = "create table "
            + TABLE_REVIEWS + "(" +
            COL_ID + " text   primary key, " +
            REVIEW_COL_AUTHOR + " text, " +
            REVIEW_COL_CONTENT + " text, " +
            REVIEW_COL_URL + " text, " +
            REVIEW_COL_MOVIE_ID + " int " +
            ");";

    public static final String CREATE_TABLE_VIDEOS = "create table "
            + TABLE_VIDEOS + "(" +
            COL_ID + " text   primary key, " +
            VIDEO_COL_ISO_639_1 + " text, " +
            VIDEO_COL_ISO_3166_1 + " text, " +
            VIDEO_COL_KEY + " text, " +
            VIDEO_COL_NAME + " text, " +
            VIDEO_COL_SITE + " text, " +
            VIDEO_COL_TYPE + " text, " +
            VIDEO_COL_SIZE + " int, " +
            VIDEO_COL_MOVIE_ID + " int " +
            ");";


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CREATE_TABLE_MOVIES);
        database.execSQL(CREATE_TABLE_REVIEWS);
        database.execSQL(CREATE_TABLE_VIDEOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MoviesDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

}
