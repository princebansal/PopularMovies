package com.nanodegree.android.popularmovies.Entity.Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nanodegree.android.popularmovies.Boundary.API.ConnectAPI;
import com.nanodegree.android.popularmovies.Boundary.Database.DataHandler;
import com.nanodegree.android.popularmovies.Boundary.Database.MoviesDbHelper;
import com.nanodegree.android.popularmovies.Control.Contracts.DatabaseContract;
import com.nanodegree.android.popularmovies.Entity.Actors.APIResult;
import com.nanodegree.android.popularmovies.Entity.Actors.Movie;
import com.nanodegree.android.popularmovies.Entity.Actors.Review;
import com.nanodegree.android.popularmovies.Entity.Actors.Video;
import com.nanodegree.android.popularmovies.Entity.Adapters.ReviewsListAdapter;
import com.nanodegree.android.popularmovies.Entity.Adapters.TrailersListAdapter;
import com.nanodegree.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FullMovieFragment extends Fragment implements ConnectAPI.ServerResponseListener, CompoundButton.OnCheckedChangeListener {

    private TextView movieTitle, movieOverview, movieRelease, movieRating, noTrailers, noReviews;
    private ImageView moviePoster;
    private RadioButton favoriteButton;
    private Toolbar toolbar;
    private RecyclerView trailersRecyclerView, reviewsRecyclerView;
    private ProgressBar trailerProgressBar, reviewsProgressBar;
    private TrailersListAdapter trailersListAdapter;

    private ConnectAPI connectAPI;
    private Movie myMovie;
    private List<Review> reviewList;
    private List<Video> videoList;
    private LinearLayout outerLayout;

    private ReviewsListAdapter reviewsListAdapter;
    private String TAG = FullMovieFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.full_movie_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setInit();
        setData();
    }


    private void init(View view) {
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        movieOverview = (TextView) view.findViewById(R.id.movieContent);
        noTrailers = (TextView) view.findViewById(R.id.no_trailers);
        noReviews = (TextView) view.findViewById(R.id.no_reviews);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        movieRelease = (TextView) view.findViewById(R.id.movieRelease);
        movieRating = (TextView) view.findViewById(R.id.movieRating);
        trailersRecyclerView = (RecyclerView) view.findViewById(R.id.movieTrailers);
        reviewsRecyclerView = (RecyclerView) view.findViewById(R.id.movieReviews);
        trailerProgressBar = (ProgressBar) view.findViewById(R.id.trailers_progress_bar);
        reviewsProgressBar = (ProgressBar) view.findViewById(R.id.reviews_progress_bar);
        favoriteButton = (RadioButton) view.findViewById(R.id.fav_button);
        outerLayout=(LinearLayout)view.findViewById(R.id.outer_layout);

        if (getArguments().getParcelable("movie") == null) {
            myMovie = null;
        } else
            myMovie = (Movie) Parcels.unwrap(getArguments().getParcelable("movie"));
        connectAPI = new ConnectAPI();
    }

    private void setInit() {

        connectAPI.setServerAuthenticateListener(this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setAutoMeasureEnabled(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setAutoMeasureEnabled(true);
        trailersRecyclerView.setLayoutManager(layoutManager1);
        reviewsRecyclerView.setLayoutManager(layoutManager2);
        favoriteButton.setChecked(false);
        favoriteButton.setOnCheckedChangeListener(this);
    }

    private void setData() {
        //Movie movie=myMovie;
        if(myMovie!=null) {
            outerLayout.setVisibility(View.VISIBLE);
            movieTitle.setText(myMovie.getOriginalTitle());
            movieOverview.setText(myMovie.getOverview());
            movieRating.setText("" + myMovie.getVoteAverage());
            movieRelease.setText("" + myMovie.getReleaseDate());
            Picasso.with(getActivity()).load(Uri.parse(myMovie.getPosterPath())).into(moviePoster);
            favoriteButton.setChecked(DataHandler.getInstance().getFavourite(getActivity(), myMovie.getId()));
            setReviews();
            setVideos();
        }else{
            outerLayout.setVisibility(View.GONE);
        }
    }

    private void setReviews() {
        reviewList = readReviewsFromContentProvider();
        if (reviewList != null && reviewList.size() != 0) {
            reviewsRecyclerView.setVisibility(View.VISIBLE);
            Log.i(TAG, "setReviews: " + reviewList.size());
            reviewsListAdapter = new ReviewsListAdapter(getActivity(), reviewList);
            reviewsRecyclerView.setAdapter(reviewsListAdapter);
        } else {
            reviewsRecyclerView.setVisibility(RecyclerView.GONE);
            connectAPI.getReviews(myMovie.getId());
        }
    }

    private void setVideos() {
        videoList = readVideosFromContentProvider();
        if (videoList != null && videoList.size() != 0) {
            trailersRecyclerView.setVisibility(View.VISIBLE);
            Log.i(TAG, "setVideos: " + videoList.size());
            trailersListAdapter = new TrailersListAdapter(getActivity(), videoList);
            trailersRecyclerView.setAdapter(trailersListAdapter);
        } else {
            trailersRecyclerView.setVisibility(RecyclerView.GONE);
            connectAPI.getVideos(myMovie.getId());
        }

    }

    private ArrayList<Review> readReviewsFromContentProvider() {
        Cursor curReviews = getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI_REVIEW, null, MoviesDbHelper.REVIEW_COL_MOVIE_ID
                + "=?", new String[]{String.valueOf(myMovie.getId())}, null);

        ArrayList<Review> reviews = new ArrayList<Review>();

        if (curReviews != null) {
            while (curReviews.moveToNext())
                reviews.add(Review.fromCursor(curReviews));
            curReviews.close();
        }
        return reviews;
    }

    private ArrayList<Video> readVideosFromContentProvider() {

        Cursor curVideos = getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI_VIDEO, null, MoviesDbHelper.VIDEO_COL_MOVIE_ID
                + "=?", new String[]{String.valueOf(myMovie.getId())}, null);

        ArrayList<Video> videos = new ArrayList<Video>();

        if (curVideos != null) {
            while (curVideos.moveToNext())
                videos.add(Video.fromCursor(curVideos));
            curVideos.close();
        }
        return videos;
    }


    @Override
    public synchronized void onRequestStarted(int code) {
        switch (code) {
            case ConnectAPI.REVIEW_CODE:
                reviewsProgressBar.setVisibility(View.VISIBLE);
                break;
            case ConnectAPI.VIDEO_CODE:
                trailerProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public synchronized void onRequestCompleted(APIResult box, int code) {
        switch (code) {
            case ConnectAPI.REVIEW_CODE:
                reviewsProgressBar.setVisibility(View.GONE);
                setReviews();
                break;
            case ConnectAPI.VIDEO_CODE:
                trailerProgressBar.setVisibility(View.GONE);
                setVideos();
        }
    }

    @Override
    public synchronized void onRequestError(APIResult box, int code) {
        switch (code) {
            case ConnectAPI.REVIEW_CODE:
                noReviews.setVisibility(View.VISIBLE);
                reviewsProgressBar.setVisibility(View.GONE);
                break;
            case ConnectAPI.VIDEO_CODE:
                noTrailers.setVisibility(View.VISIBLE);
                trailerProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        DataHandler.getInstance().markAsFavourite(getActivity(), myMovie.getId(), isChecked);
    }
}
