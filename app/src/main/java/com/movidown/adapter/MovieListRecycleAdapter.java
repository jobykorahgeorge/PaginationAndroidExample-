package com.movidown.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.movidown.R;
import com.movidown.domain.MovieDetail;
import com.movidown.interfaces.LoadNextPageCallback;
import com.movidown.utilities.PublicValues;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anooj on 20-Apr-18.
 */

public class MovieListRecycleAdapter extends RecyclerView.Adapter {

    boolean isLoading=false;
    private GridLayoutManager layoutManager = null;
    private LoadNextPageCallback onLoadMoreItems;


    private ArrayList<MovieDetail> movieResult = new ArrayList<>();
    Context context;

    public MovieListRecycleAdapter(ArrayList<MovieDetail> movieResult,Context context1,RecyclerView recyclerView) {
        this.movieResult = movieResult;
        this.context = context1;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemsCount = 0;
                int currentItems = 0;
                if (layoutManager != null) {
                    totalItemsCount = layoutManager.getItemCount();
                    currentItems = layoutManager.findLastVisibleItemPosition();
                }
                if (!isLoading && (totalItemsCount <= currentItems + PublicValues.PAGE_THRESHOLD)) {
                    //about to reach end of the page
                    if (onLoadMoreItems != null) {
                        isLoading = true;
                        //callback for loading next page
                        onLoadMoreItems.onLoadMorePageItems();
                    }
                }
            }
        });
    }

    public void setOnLoadMore(LoadNextPageCallback onLoadMore) {
        onLoadMoreItems = onLoadMore;
    }
    //callback method to trigger after one page is loaded
    public void setsLoading(boolean loading) {
        // setting loading status
        isLoading = loading;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_recycler_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder holder1=(MovieViewHolder)holder ;

        holder1.movie_name.setText(movieResult.get(position).getTitle());
        holder1.year.setText(movieResult.get(position).getYear());
        holder1.rating.setText(movieResult.get(position).getRating());
        Glide.with(context).load(movieResult.get(position).getMedium_cover_image()).into(((MovieViewHolder) holder).movie_img);
    }

    @Override
    public int getItemCount() {
        return movieResult.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_image)
        ImageView movie_img;
        @BindView(R.id.movie_name)
        TextView movie_name;
        @BindView(R.id.year)
        TextView year;
        @BindView(R.id.rating)
        TextView rating;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
