package com.nanodegree.android.popularmovies;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.nanodegree.android.popularmovies.Volley.AppController;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shalini on 16-07-2015.
 */
public class HomeActivityNew extends AppCompatActivity {


    public static int POPULAR_MOVIE_FILTER=0;
    public static int TOP_RATED_MOVIE_FILTER=1;
    public static List<Movie> moviesList;

    public int filter=POPULAR_MOVIE_FILTER;
    private Toolbar toolbar;
    private ViewPager pager;
    private ProgressDialog dialog;

    //Put your own api key here
    public String API_KEY="";
    private String moviesPopularUrl = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
    private String moviesMostRatedUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
    private boolean moviesCallOver = false;


    private RecyclerView recyclerView;
    private HomeListAdapter adapter;
    private int MODE = 0;
    public static Movie myMovie;
    private TextView noContentView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout_new);

        init();
        setInit();
        setData();
    }
    private void init() {
        //tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        dialog = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter=new HomeListAdapter(this,new ArrayList<Movie>());
        noContentView = (TextView) findViewById(R.id.nocontent_text);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
    }

    private void setInit() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Popular Movies");
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
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
                        swipeRefreshLayout.setRefreshing(false);//this should be false for rotation
                    }
                }, 5000);

                setData();
                //adapter.notifyDataSetChanged();
                swipeRefreshLayout.setEnabled(true);
            }


        });
        //ab.setDisplayHomeAsUpEnabled(true);


        /*swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//this should be false for rotation
                    }
                }, 5000);

                setData();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setEnabled(true);
            }


        });
*/
        dialog.setCancelable(true);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading Movies...");

    }

    public void setData() {
        dialog.show();
        String url=null;
        if(filter==POPULAR_MOVIE_FILTER){
            url=moviesPopularUrl;
        }else{
            url=moviesMostRatedUrl;
        }
        StringRequest moviesRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("mRes",response);
                if (response != null) {
                    response=response.trim();
                    //response=response.substring(18,response.length()-1);

                    //Log.d("mResmod",response.substring(0,100)+"\n\n\n******************\n"+response.substring(response.length()-100,response.length()));
                    APIResult apiResult=new Gson().fromJson(response,APIResult.class);
                    moviesList = apiResult.getResults();
                    moviesCallOver = true;
                    if (moviesCallOver) {
                        dialog.dismiss();
                        if (HomeActivityNew.moviesList != null && HomeActivityNew.moviesList.size() != 0) {
                            Toast.makeText(HomeActivityNew.this,"Refreshed",Toast.LENGTH_SHORT).show();
                            adapter = new HomeListAdapter(HomeActivityNew.this, moviesList);
                            recyclerView.setAdapter(adapter);
                            noContentView.setVisibility(TextView.GONE);
                        } else {
                            recyclerView.setVisibility(RecyclerView.GONE);
                            noContentView.setVisibility(TextView.VISIBLE);
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                moviesList = new ArrayList<>();
                moviesCallOver = false;
                showToast();
                dialog.dismiss();
            }
        });
        moviesRequest.setRetryPolicy(new DefaultRetryPolicy(10000,2,1));
        AppController.getInstance().addToRequestQueue(moviesRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.popular:
                filter=POPULAR_MOVIE_FILTER;
                setData();
                break;
            case R.id.most_rated:
                filter=TOP_RATED_MOVIE_FILTER;
                setData();
                break;
        }
        return true;
    }

    private void showToast() {
        Toast.makeText(this,"Error Fetching!",Toast.LENGTH_SHORT).show();
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
            View view=new View(HomeActivityNew.this);
            try {
                view= inflater.inflate(R.layout.home_pager_recycler_row, parent, false);
            }catch (Exception e){
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
            Picasso.with(HomeActivityNew.this).load(Uri.parse(movie.getPosterPath())).into(holder.moviePoster);

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

                Intent intent=new Intent(HomeActivityNew.this,FullMovieActivityNew.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable("movie", Parcels.wrap(data.get(getAdapterPosition())));
                intent.putExtra("extra_bundle",bundle);
                startActivity(intent);

            }
        }
    }
}
