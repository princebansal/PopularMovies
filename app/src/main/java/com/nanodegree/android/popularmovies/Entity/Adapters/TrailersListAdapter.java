package com.nanodegree.android.popularmovies.Entity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.popularmovies.Entity.Actors.Video;
import com.nanodegree.android.popularmovies.R;

import java.util.List;

public class TrailersListAdapter extends RecyclerView.Adapter<TrailersListAdapter.TrailerViewHolder> {
    private final List<Video> data;
    private LayoutInflater inflater;
    private Context c;

    public TrailersListAdapter(Context context, List<Video> list) {
        data = list;
        c = context;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public TrailersListAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new View(c);
        try {
            view = inflater.inflate(R.layout.trailers_recycler_row, parent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TrailerViewHolder trailersViewHolder = new TrailerViewHolder(view);
        return trailersViewHolder;
    }


    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Video video = data.get(position);
        holder.videoName.setText(video.getName());
        holder.videoSite.setText(video.getSite());
    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoName,videoSite;
        public TrailerViewHolder(View itemView) {
            super(itemView);

            videoName = (TextView) itemView.findViewById(R.id.video_name);
            videoSite = (TextView) itemView.findViewById(R.id.video_site);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Video selected=data.get(getAdapterPosition());
            if(selected.getSite().equalsIgnoreCase("youtube")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Video.VIDEO_URL_YOUTUBE_PREFIX+selected.getKey()));
                c.startActivity(intent);
            }

        }
    }
}