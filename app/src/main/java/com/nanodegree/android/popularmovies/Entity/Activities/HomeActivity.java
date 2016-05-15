package com.nanodegree.android.popularmovies.Entity.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nanodegree.android.popularmovies.Entity.Actors.Movie;
import com.nanodegree.android.popularmovies.Entity.Fragments.FullMovieFragment;
import com.nanodegree.android.popularmovies.Entity.Fragments.HomeFragment;
import com.nanodegree.android.popularmovies.R;

import org.parceler.Parcels;

/**
 * Created by Prince Bansal Local on 15-05-2016.
 */
public class HomeActivity extends AppCompatActivity {

    private boolean isTablet = false;
    private FrameLayout fragmentHolder1, fragmentHolder2;
    private Toolbar toolbar;
    public static int filter = HomeFragment.POPULAR_MOVIE_FILTER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setInit();
        setData();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                getResources().getBoolean(R.bool.isTablet)) {
            fragmentHolder1 = (FrameLayout) findViewById(R.id.fragment_holder_one);
            fragmentHolder2 = (FrameLayout) findViewById(R.id.fragment_holder_two);
            isTablet = true;
        } else {
            fragmentHolder1 = (FrameLayout) findViewById(R.id.fragment_holder);
        }
    }

    private void setInit() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Popular Movies");

    }

    private void setData() {
        updateHomeFragment(filter);
        if(isTablet)
        updateFullMovieFragment(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popular:
                if(getFragmentManager().findFragmentByTag("home")==null){
                    filter = HomeFragment.POPULAR_MOVIE_FILTER;
                    updateHomeFragment(filter);
                }
                else if (filter != HomeFragment.POPULAR_MOVIE_FILTER) {
                    filter = HomeFragment.POPULAR_MOVIE_FILTER;
                    updateHomeFragment(filter);
                }
                break;
            case R.id.most_rated:
                if(getFragmentManager().findFragmentByTag("home")==null){
                    filter = HomeFragment.TOP_RATED_MOVIE_FILTER;
                    updateHomeFragment(filter);
                }
                else if (filter != HomeFragment.TOP_RATED_MOVIE_FILTER) {
                    filter = HomeFragment.TOP_RATED_MOVIE_FILTER;
                    updateHomeFragment(filter);
                }
                break;
            case R.id.favourites:
                if(getFragmentManager().findFragmentByTag("home")==null){
                    filter = HomeFragment.FAVOURITES_FILTER;
                    updateHomeFragment(filter);
                }
                else if (filter != HomeFragment.FAVOURITES_FILTER) {
                    filter = HomeFragment.FAVOURITES_FILTER;
                    updateHomeFragment(filter);
                }
                break;
        }
        return true;
    }

    public void updateHomeFragment(int filter) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("filter", filter);
        HomeFragment fragment=new HomeFragment();
        fragment.setArguments(bundle);
        transaction.replace(isTablet ? R.id.fragment_holder_one : R.id.fragment_holder, fragment,"home");
        transaction.commit();
    }

    public void updateFullMovieFragment(Movie movie) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fullFragment = new FullMovieFragment();
        Bundle bundle = new Bundle();
        if (movie != null)
            bundle.putParcelable("movie", Parcels.wrap(movie));
        else
            bundle.putParcelable("movie", null);
        fullFragment.setArguments(bundle);
        transaction.replace(isTablet ? R.id.fragment_holder_two : R.id.fragment_holder, fullFragment);
        transaction.commit();
    }

}
