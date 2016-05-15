package com.nanodegree.android.popularmovies.Boundary.Database;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nanodegree.android.popularmovies.Control.Contracts.DatabaseContract;
import com.nanodegree.android.popularmovies.Entity.Actors.APIResult;
import com.nanodegree.android.popularmovies.Entity.Actors.Movie;
import com.nanodegree.android.popularmovies.Entity.Actors.Review;
import com.nanodegree.android.popularmovies.Entity.Actors.Video;
import com.nanodegree.android.popularmovies.Entity.Fragments.HomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince Bansal Local on 14-05-2016.
 */
public class DataHandler {

    private static DataHandler ourInstance = new DataHandler();
    private static final String TAG = DataHandler.class.getSimpleName();

    private SharedPreferences preferences;
    public static DataHandler getInstance() {
        return ourInstance;
    }


    private DataHandler() {
    }

    public void saveMovies(Context context, APIResult<Movie> result) {

        try {
            ArrayList<Movie> remoteMovies = (ArrayList<Movie>) result.getResults();
            if (remoteMovies == null) {
                Log.i(TAG, "performRequest: remoteMovies is null");
                remoteMovies = new ArrayList<Movie>();
            }

            if (remoteMovies.size() == 0) {
                Log.d("popularMovies", TAG + "> No server changes to update local database");
            } else {
                Log.d("popularMovies", TAG + "> Updating local database with remote changes");

                context.getContentResolver().delete(DatabaseContract.CONTENT_URI_MOVIE, null, null);
                context.getContentResolver().delete(DatabaseContract.CONTENT_URI_REVIEW, null, null);
                context.getContentResolver().delete(DatabaseContract.CONTENT_URI_VIDEO, null, null);
                // Updating local movies
                int i = 0;
                ContentValues moviesToLocalValues[] = new ContentValues[remoteMovies.size()];
                for (Movie localMovie : remoteMovies) {
                    Log.d("popularMovies", TAG + "> Remote -> Local [" + localMovie.id + "]");
                    moviesToLocalValues[i++] = localMovie.getContentValues();
                }
                context.getContentResolver().bulkInsert(DatabaseContract.CONTENT_URI_MOVIE, moviesToLocalValues);
            }

            Log.d("popularMovies", TAG + "> Finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveReview(Context context, APIResult<Review> result,int movieId) {

        try {
            ArrayList<Review> remoteReviews = (ArrayList<Review>) result.getResults();
            if (remoteReviews == null) {
                Log.i(TAG, "performRequest: remoteReviews is null");
                remoteReviews = new ArrayList<>();
            }


            ArrayList<Review> localReviews = new ArrayList<>();
            Cursor curReviews = context.getContentResolver().query(DatabaseContract.CONTENT_URI_REVIEW, null, null, null, null);
            if (curReviews != null) {
                while (curReviews.moveToNext()) {
                    localReviews.add(Review.fromCursor(curReviews));
                }
                curReviews.close();
            }


            // See what Remote reviews are missing on Local
            ArrayList<Review> reviewsToLocal = new ArrayList<>();
            for (Review remReview : remoteReviews) {
                if (!localReviews.contains(remReview)) {
                    remReview.setMovieId(movieId);
                    reviewsToLocal.add(remReview);
                }
            }

            if (reviewsToLocal.size() == 0) {
                Log.d(TAG, "> No server changes to update local database");
            } else {
                Log.d(TAG, "> Updating local database with remote changes");

                int i = 0;
                ContentValues showsToLocalValues[] = new ContentValues[reviewsToLocal.size()];
                for (Review localReview : reviewsToLocal) {
                    Log.d(TAG, "> Remote -> Local [" + localReview.id + "]");
                    showsToLocalValues[i++] = localReview.getContentValues();
                }
                context.getContentResolver().bulkInsert(DatabaseContract.CONTENT_URI_REVIEW, showsToLocalValues);
            }

            Log.d(TAG, "> Finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveVideo(Context context, APIResult<Video> result, int movieId) {

        try {
            ArrayList<Video> remoteVideos = (ArrayList<Video>) result.getResults();
            if (remoteVideos == null) {
                Log.i(TAG, "performRequest: remoteVideos is null");
                remoteVideos = new ArrayList<>();
            }


            ArrayList<Video> localVideos = new ArrayList<>();
            Cursor curVideos = context.getContentResolver().query(DatabaseContract.CONTENT_URI_VIDEO, null, null, null, null);
            if (curVideos != null) {
                while (curVideos.moveToNext()) {
                    localVideos.add(Video.fromCursor(curVideos));
                }
                curVideos.close();
            }


            // See what Remote videos are missing on Local
            ArrayList<Video> videosToLocal = new ArrayList<>();
            for (Video remVideo : remoteVideos) {
                if (!localVideos.contains(remVideo)) {
                    remVideo.setMovieId(movieId);
                    videosToLocal.add(remVideo);
                }
            }

            if (videosToLocal.size() == 0) {
                Log.d(TAG, "> No server changes to update local database");
            } else {
                Log.d(TAG, "> Updating local database with remote changes");

                int i = 0;
                ContentValues showsToLocalValues[] = new ContentValues[videosToLocal.size()];
                for (Video localVideo : videosToLocal) {
                    Log.d(TAG, "> Remote -> Local [" + localVideo.id + "]");
                    showsToLocalValues[i++] = localVideo.getContentValues();
                }
                context.getContentResolver().bulkInsert(DatabaseContract.CONTENT_URI_VIDEO, showsToLocalValues);
            }

            Log.d(TAG, "> Finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsFavourite(Context context, int movieId, boolean value){
        try {
            ContentValues contentValues=new ContentValues();
            contentValues.put(MoviesDbHelper.MOVIE_COL_FAVORITE,String.valueOf(value));
            int rows=context.getContentResolver().update(DatabaseContract.CONTENT_URI_MOVIE,contentValues,MoviesDbHelper.COL_ID+"=?",new String[]{String.valueOf(movieId)});
            Log.d("popularMovies", TAG + "> Finished."+rows +"rows afffected:"+value);
            Cursor cursor=context.getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE,null,MoviesDbHelper.COL_ID+"="+movieId,null,null);
            Movie movie=null;
            while(cursor.moveToNext()) {
                movie= Movie.fromCursor(cursor);
            }
            Log.d(TAG,"movie:"+movie.getId()+":"+movie.getOriginalTitle()+":"+movie.isFavourite());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getFavourite(Context context, int movieId){
        try {
            Cursor cursor=context.getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE,null,MoviesDbHelper.COL_ID+"="+movieId,null,null);
            Movie movie=null;
            while(cursor.moveToNext()) {
                 movie= Movie.fromCursor(cursor);
            }
            Log.d(TAG,"movie:"+movie.getId()+":"+movie.getOriginalTitle()+":"+movie.isFavourite());
            cursor.close();
            return movie!=null?movie.isFavourite():false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Movie> getFavouriteMovies(Context context) {
        try {
            Cursor cursor=context.getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE,null,MoviesDbHelper.MOVIE_COL_FAVORITE+"=?"
                    ,new String[]{"true"},null);
           List<Movie> list=new ArrayList<>();
            while(cursor.moveToNext()) {
                list.add(Movie.fromCursor(cursor));
            }
            //Log.d(TAG,"movie:"+movie.getId()+":"+movie.getOriginalTitle()+":"+movie.isFavourite());
            cursor.close();
            return list!=null?list:new ArrayList<Movie>();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int getMoviesSortType(Context context) {
        preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("sortType", HomeFragment.POPULAR_MOVIE_FILTER);
    }
    public void saveMovieSortType(Context context,int sortType){
        preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("sortType",sortType);
        editor.commit();
    }
}
