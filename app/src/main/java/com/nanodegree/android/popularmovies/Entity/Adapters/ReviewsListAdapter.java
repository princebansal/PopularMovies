package com.nanodegree.android.popularmovies.Entity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.popularmovies.Entity.Actors.Review;
import com.nanodegree.android.popularmovies.Entity.Actors.Video;
import com.nanodegree.android.popularmovies.R;

import java.util.List;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ReviewViewHolder> {
    private final List<Review> data;
    private LayoutInflater inflater;
    private Context c;

    public ReviewsListAdapter(Context context, List<Review> list) {
        data = list;
        c = context;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public ReviewsListAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new View(c);
        try {
            view = inflater.inflate(R.layout.reviews_recycler_row, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReviewViewHolder reviewsViewHolder = new ReviewViewHolder(view);
        return reviewsViewHolder;
    }


    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = data.get(position);
        holder.reviewContent.setText(review.getContent());
        holder.reviewAuthor.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewContent,reviewAuthor;
        public ReviewViewHolder(View itemView) {
            super(itemView);

            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
        }



    }
}