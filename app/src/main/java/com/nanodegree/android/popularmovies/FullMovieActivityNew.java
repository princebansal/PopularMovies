package com.nanodegree.android.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nanodegree.android.popularmovies.Volley.AppController;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * Created by shalini on 30-07-2015.
 */
public class FullMovieActivityNew extends AppCompatActivity {

    private TextView movieTitle,movieOverview,movieRelease,movieRating;
    private ImageView moviePoster;
    private Toolbar toolbar;
    private Movie myMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_movie_layout);

        init();
        //mode=getIntent().getIntExtra("mode",0);
        //if(mode==0)
        //myMovie=HomeFragment.myMovie;
        setInit();
        setData();
    }

    private void init() {
        movieTitle=(TextView)findViewById(R.id.movieTitle);
        movieOverview=(TextView)findViewById(R.id.movieContent);
        moviePoster=(ImageView)findViewById(R.id.moviePoster);
        movieRelease=(TextView)findViewById(R.id.movieRelease);
        movieRating=(TextView)findViewById(R.id.movieRating);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        myMovie=(Movie) Parcels.unwrap(getIntent().getBundleExtra("extra_bundle").getParcelable("movie"));
    }

    private void setInit() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        toolbar.setNavigationOnClickListener(R.class.asSubclass(.asSubclass(.asSubclass(.asSubclass(.getAnnotation(getExternalCacheDir()))))))
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        //Movie movie=myMovie;
        movieTitle.setText(myMovie.getOriginalTitle());
        movieOverview.setText(myMovie.getOverview());
        movieRating.setText(""+myMovie.getVoteAverage());
        movieRelease.setText(""+myMovie.getReleaseDate());
        Picasso.with(this).load(Uri.parse(myMovie.getPosterPath())).into(moviePoster);
    }
}
