package com.nanodegree.android.popularmovies.Boundary.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.nanodegree.android.popularmovies.Control.Contracts.DatabaseContract;
import static com.nanodegree.android.popularmovies.Control.Contracts.DatabaseContract.AUTHORITY;


public class MoviesContentProvider extends ContentProvider {

    public static final UriMatcher URI_MATCHER = buildUriMatcher();
    public static final String PATH_MOVIE = "movies";
    public static final String PATH_REVIEW = "reviews";
    public static final String PATH_VIDEO = "videos";
    public static final int PATH_TOKEN_MOVIE = 100;
    public static final int PATH_TOKEN_REVIEW = 101;
    public static final int PATH_TOKEN_VIDEO = 102;
    public static final String PATH_FOR_ID_MOVIE = "workouts/*";
    public static final String PATH_FOR_ID_REVIEW = "diets/*";
    public static final String PATH_FOR_ID_VIDEO = "gyms/*";
    public static final int PATH_FOR_ID_TOKEN_MOVIE = 200;
    public static final int PATH_FOR_ID_TOKEN_REVIEW = 201;
    public static final int PATH_FOR_ID_TOKEN_VIDEO = 202;

    // Uri Matcher for the content provider
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = AUTHORITY;
        matcher.addURI(authority, PATH_MOVIE, PATH_TOKEN_MOVIE);
        matcher.addURI(authority, PATH_REVIEW, PATH_TOKEN_REVIEW);
        matcher.addURI(authority, PATH_VIDEO, PATH_TOKEN_VIDEO);
        matcher.addURI(authority, PATH_FOR_ID_REVIEW, PATH_FOR_ID_TOKEN_REVIEW);
        matcher.addURI(authority, PATH_FOR_ID_MOVIE, PATH_FOR_ID_TOKEN_MOVIE);
        matcher.addURI(authority, PATH_FOR_ID_VIDEO, PATH_FOR_ID_TOKEN_VIDEO);
        return matcher;
    }

    // Content Provider stuff

    private MoviesDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();
        dbHelper = new MoviesDbHelper(ctx);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN_REVIEW:
                return DatabaseContract.CONTENT_TYPE_DIR_REVIEW;
            case PATH_TOKEN_MOVIE:
                return DatabaseContract.CONTENT_TYPE_DIR_MOVIE;
            case PATH_TOKEN_VIDEO:
                return DatabaseContract.CONTENT_TYPE_DIR_VIDEO;
            case PATH_FOR_ID_TOKEN_REVIEW:
                return DatabaseContract.CONTENT_ITEM_TYPE_REVIEW;
            case PATH_FOR_ID_TOKEN_MOVIE:
                return DatabaseContract.CONTENT_ITEM_TYPE_MOVIE;
            case PATH_FOR_ID_TOKEN_VIDEO:
                return DatabaseContract.CONTENT_ITEM_TYPE_VIDEO;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            // retrieve movies list
            case PATH_TOKEN_REVIEW: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(MoviesDbHelper.TABLE_REVIEWS);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN_REVIEW: {
                int reviewId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(MoviesDbHelper.TABLE_REVIEWS);
                builder.appendWhere(MoviesDbHelper.COL_ID + "=" + reviewId);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_TOKEN_MOVIE: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(MoviesDbHelper.TABLE_MOVIES);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN_MOVIE: {
                int movieId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(MoviesDbHelper.TABLE_MOVIES);
                builder.appendWhere(MoviesDbHelper.COL_ID + "=" + movieId);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_TOKEN_VIDEO: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(MoviesDbHelper.TABLE_VIDEOS);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN_VIDEO: {
                int videoId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(MoviesDbHelper.TABLE_VIDEOS);
                builder.appendWhere(MoviesDbHelper.COL_ID + "=" + videoId);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        switch (token) {
            case PATH_TOKEN_MOVIE: {
                long id = db.insert(MoviesDbHelper.TABLE_MOVIES, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DatabaseContract.CONTENT_URI_MOVIE.buildUpon().appendPath(String.valueOf(id)).build();
            }
            case PATH_TOKEN_REVIEW: {
                long id = db.insert(MoviesDbHelper.TABLE_REVIEWS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DatabaseContract.CONTENT_URI_REVIEW.buildUpon().appendPath(String.valueOf(id)).build();
            }
            case PATH_TOKEN_VIDEO: {
                long id = db.insert(MoviesDbHelper.TABLE_VIDEOS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return DatabaseContract.CONTENT_URI_VIDEO.buildUpon().appendPath(String.valueOf(id)).build();
            }

            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        int rowsDeleted = -1;
        switch (token) {
            case (PATH_TOKEN_MOVIE):
                rowsDeleted = db.delete(MoviesDbHelper.TABLE_MOVIES, selection, selectionArgs);
                break;
            case (PATH_TOKEN_REVIEW):
                rowsDeleted = db.delete(MoviesDbHelper.TABLE_REVIEWS, selection, selectionArgs);
                break;
            case (PATH_TOKEN_VIDEO):
                rowsDeleted = db.delete(MoviesDbHelper.TABLE_VIDEOS, selection, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN_MOVIE):
                String movieIdWhereClause = MoviesDbHelper.COL_ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    movieIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(MoviesDbHelper.TABLE_MOVIES, movieIdWhereClause, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN_REVIEW):
                String reviewIdWhereClause = MoviesDbHelper.COL_ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    reviewIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(MoviesDbHelper.TABLE_REVIEWS, reviewIdWhereClause, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN_VIDEO):
                String videoIdWhereClause = MoviesDbHelper.COL_ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    videoIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(MoviesDbHelper.TABLE_VIDEOS, videoIdWhereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // Notifying the changes, if there are any
        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    /**
     * Man..I'm tired..
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        switch (token) {
            case PATH_TOKEN_MOVIE: {
                long id = db.update(MoviesDbHelper.TABLE_MOVIES, values, selection, selectionArgs);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return (int) id;
            }
            case PATH_TOKEN_REVIEW: {
                long id = db.insert(MoviesDbHelper.TABLE_REVIEWS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return (int) id;
            }
            case PATH_TOKEN_VIDEO: {
                long id = db.insert(MoviesDbHelper.TABLE_VIDEOS, null, values);
                if (id != -1)
                    getContext().getContentResolver().notifyChange(uri, null);
                return (int) id;
            }
            default: {
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
            }
        }
    }

}