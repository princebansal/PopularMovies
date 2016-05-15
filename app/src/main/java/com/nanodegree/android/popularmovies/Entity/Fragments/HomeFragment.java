package com.nanodegree.android.popularmovies.Entity.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.android.popularmovies.Boundary.API.ConnectAPI;
import com.nanodegree.android.popularmovies.Boundary.Database.DataHandler;
import com.nanodegree.android.popularmovies.Control.Contracts.DatabaseContract;
import com.nanodegree.android.popularmovies.Entity.Activities.HomeActivity;
import com.nanodegree.android.popularmovies.Entity.Actors.APIResult;
import com.nanodegree.android.popularmovies.Entity.Actors.Movie;
import com.nanodegree.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ConnectAPI.ServerResponseListener {


    public static int POPULAR_MOVIE_FILTER = 0;
    public static int TOP_RATED_MOVIE_FILTER = 1;
    public static final int FAVOURITES_FILTER = 2;
    public static List<Movie> moviesList;

    private ProgressDialog dialog;

    private RecyclerView recyclerView;
    private HomeListAdapter adapter;
    private int filter;
    private TextView noContentView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ConnectAPI connectAPI;
    private String TAG = HomeFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        init(rootView);
        setInit();
        setData();
        return rootView;
    }


    private void init(View view) {
        dialog = new ProgressDialog(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new HomeListAdapter(getActivity(), new ArrayList<Movie>());
        noContentView = (TextView) view.findViewById(R.id.nocontent_text);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        filter = getArguments().getInt("filter");
        Log.i(TAG, "init: filter:" + filter);
        connectAPI = new ConnectAPI();
    }

    private void setInit() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//getActivity() should be false for rotation
                    }
                }, 5000);

                setData();
                //adapter.notifyDataSetChanged();
                swipeRefreshLayout.setEnabled(true);
            }


        });

        dialog.setCancelable(true);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading Movies...");

        connectAPI.setServerAuthenticateListener(this);

    }

    public void setData() {
        if (filter == FAVOURITES_FILTER) {
            setData(DataHandler.getInstance().getFavouriteMovies(getActivity()));
        } else if (DataHandler.getInstance().getMoviesSortType(getActivity()) == filter) {
            moviesList = readMoviesFromContentProvider();

            if (moviesList != null && moviesList.size() != 0) {
                Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.VISIBLE);
                for (int i = 0; i < moviesList.size(); i++) {
                    Log.i(TAG, "setData: " + moviesList.get(i).getId());
                }
                noContentView.setVisibility(View.GONE);
                adapter = new HomeListAdapter(getActivity(), moviesList);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(RecyclerView.GONE);
                noContentView.setVisibility(TextView.VISIBLE);
                connectAPI.getMovies(filter);
            }
        } else {
            recyclerView.setVisibility(RecyclerView.GONE);
            noContentView.setVisibility(TextView.VISIBLE);
            connectAPI.getMovies(filter);

        }
    }

    public void setData(List<Movie> list) {
        moviesList = list;

        if (moviesList != null && moviesList.size() != 0) {
            Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.VISIBLE);
            for (int i = 0; i < moviesList.size(); i++) {
                Log.i(TAG, "setData: " + moviesList.get(i).getId());
            }
            noContentView.setVisibility(View.GONE);
            adapter = new HomeListAdapter(getActivity(), moviesList);
            recyclerView.setAdapter(adapter);
            noContentView.setVisibility(TextView.GONE);
        } else {
            recyclerView.setVisibility(RecyclerView.GONE);
            noContentView.setVisibility(TextView.VISIBLE);
            noContentView.setText("No favourites");
        }
    }


    private void showToast() {
        Toast.makeText(getActivity(), "Error Fetching!", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<Movie> readMoviesFromContentProvider() {
        Cursor curMovies = getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI_MOVIE, null, null, null, null);

        ArrayList<Movie> movies = new ArrayList<Movie>();

        if (curMovies != null) {
            while (curMovies.moveToNext())
                movies.add(Movie.fromCursor(curMovies));
            curMovies.close();
        }
        return movies;
    }


    @Override
    public synchronized void onRequestStarted(int code) {
        dialog.show();
    }

    @Override
    public synchronized void onRequestCompleted(APIResult box, int code) {
        dialog.cancel();
        setData();
    }

    @Override
    public synchronized void onRequestError(APIResult box, int code) {
        dialog.cancel();
        recyclerView.setVisibility(RecyclerView.GONE);
        noContentView.setVisibility(TextView.VISIBLE);
    }


    private class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeViewHolder> {
        private final List<Movie> data;
        private LayoutInflater inflater;
        private Context c;

        public HomeListAdapter(Context context, List<Movie> list) {
            data = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public HomeListAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new View(getActivity());
            try {
                view = inflater.inflate(R.layout.home_pager_recycler_row, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HomeViewHolder homeViewHolder = new HomeViewHolder(view);
            return homeViewHolder;
        }


        @Override
        public void onBindViewHolder(HomeViewHolder holder, int position) {
            Movie movie = data.get(position);
            holder.movieName.setText(movie.getOriginalTitle());
            //setStars(holder,0);
            Picasso.with(getActivity()).load(Uri.parse(movie.getPosterPath())).into(holder.moviePoster);

        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView moviePoster;
            TextView movieName;

            public HomeViewHolder(View itemView) {
                super(itemView);
                moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
                itemView.setOnClickListener(this);

                movieName = (TextView) itemView.findViewById(R.id.movie_name);
            }


            @Override
            public void onClick(View v) {

                ((HomeActivity) getActivity()).updateFullMovieFragment(data.get(getAdapterPosition()));

            }
        }
    }
}
