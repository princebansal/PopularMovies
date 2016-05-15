
package com.nanodegree.android.popularmovies.Boundary.API;

import android.text.TextUtils;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nanodegree.android.popularmovies.Boundary.Controllers.AppController;
import com.nanodegree.android.popularmovies.Boundary.Database.DataHandler;
import com.nanodegree.android.popularmovies.Entity.Fragments.HomeFragment;
import com.nanodegree.android.popularmovies.Entity.Actors.APIResult;
import com.nanodegree.android.popularmovies.Entity.Actors.Movie;
import com.nanodegree.android.popularmovies.Entity.Actors.Review;
import com.nanodegree.android.popularmovies.Entity.Actors.Video;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class ConnectAPI {

    //Constants
    public static final int MOVIE_CODE = 0;
    public static final int REVIEW_CODE = 1;
    public static final int VIDEO_CODE = 2;
    public static final int GENERIC_CODE = -1;


    //Declared URLs

    private final String API_KEY="YOUR_API_KEY";
    private String moviesPopularUrl = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
    private String moviesMostRatedUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
    private String videoUrl = "http://api.themoviedb.org/3/movie/%d/videos?api_key="+API_KEY;
    private String reviewsUrl = "http://api.themoviedb.org/3/movie/%d/reviews?api_key="+API_KEY;

    private AppController appController;
    private ServerResponseListener mServerAuthenticateListener;
    private long REQUEST_TIMEOUT = 30;
    private final String TAG=ConnectAPI.class.getSimpleName();

    public ConnectAPI() {
        appController = AppController.getInstance();
    }

    public void getMovies(final int filter){
        String url=null;
        if(filter== HomeFragment.POPULAR_MOVIE_FILTER){
            url=moviesPopularUrl;
        }else{
            url=moviesMostRatedUrl;
        }
        mServerAuthenticateListener.onRequestStarted(MOVIE_CODE);
        StringRequest moviesRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mRes",response);
                if (response != null) {
                    response=response.trim();

                    if(validateResponse(response)) {
                        APIResult<Movie> apiResult = new Gson().fromJson(response, new TypeToken<APIResult<Movie>>(){}.getType());
                        DataHandler.getInstance().saveMovies(appController.getApplicationContext(),apiResult);
                        Log.i(TAG, "onResponse: moviesSaved");
                        if(apiResult.getResults().size()!=0) {
                            mServerAuthenticateListener.onRequestCompleted(apiResult, MOVIE_CODE);
                            DataHandler.getInstance().saveMovieSortType(appController.getApplicationContext(), filter);
                        }
                        else{
                            mServerAuthenticateListener.onRequestError(apiResult,MOVIE_CODE);
                        }
                    }else{
                        mServerAuthenticateListener.onRequestError(null,MOVIE_CODE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mServerAuthenticateListener.onRequestError(null,MOVIE_CODE);
            }
        });
        moviesRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2,1));
        AppController.getInstance().addToRequestQueue(moviesRequest);
    }

    public void getVideos(final int movieId){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        PrintStream printStream=new PrintStream(byteArrayOutputStream);
        printStream.printf(videoUrl,movieId);
        String url= null;
        try {
            url = byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mServerAuthenticateListener.onRequestStarted(VIDEO_CODE);
        StringRequest moviesRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mRes",response);
                if (response != null) {
                    response=response.trim();

                    if(validateResponse(response)) {
                        APIResult<Video> apiResult = new Gson().fromJson(response, new TypeToken<APIResult<Video>>() {
                        }.getType());
                        DataHandler.getInstance().saveVideo(appController.getApplicationContext(), apiResult, movieId);
                        if (apiResult.getResults().size() != 0)
                            mServerAuthenticateListener.onRequestCompleted(apiResult, VIDEO_CODE);
                        else{
                            mServerAuthenticateListener.onRequestError(apiResult,VIDEO_CODE);
                        }
                    }else{
                        mServerAuthenticateListener.onRequestError(null,VIDEO_CODE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mServerAuthenticateListener.onRequestError(null,VIDEO_CODE);
            }
        });
        moviesRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2,1));
        AppController.getInstance().addToRequestQueue(moviesRequest);
    }

    public void getReviews(final int movieId){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        PrintStream printStream=new PrintStream(byteArrayOutputStream);
        printStream.printf(reviewsUrl,movieId);
        String url= null;
        try {
            url = byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mServerAuthenticateListener.onRequestStarted(REVIEW_CODE);
        StringRequest moviesRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mRes",response);
                if (response != null) {
                    response=response.trim();

                    if(validateResponse(response)) {
                        APIResult<Review> apiResult = new Gson().fromJson(response, new TypeToken<APIResult<Review>>() {
                        }.getType());
                        DataHandler.getInstance().saveReview(appController.getApplicationContext(), apiResult, movieId);
                        if (apiResult.getResults().size() != 0)
                            mServerAuthenticateListener.onRequestCompleted(apiResult, REVIEW_CODE);
                        else{
                            mServerAuthenticateListener.onRequestError(apiResult,REVIEW_CODE);
                        }
                    }else{
                        mServerAuthenticateListener.onRequestError(null,REVIEW_CODE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mServerAuthenticateListener.onRequestError(null,REVIEW_CODE);
            }
        });
        moviesRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2,1));
        AppController.getInstance().addToRequestQueue(moviesRequest);
    }


    private boolean validateResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }
        try {
            new JSONObject(response);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void setServerAuthenticateListener(ServerResponseListener listener) {
        mServerAuthenticateListener = listener;
    }

    public interface ServerResponseListener {
        void onRequestStarted(int code);

        void onRequestCompleted(APIResult box, int code);

        void onRequestError(APIResult box, int code);
    }
}
