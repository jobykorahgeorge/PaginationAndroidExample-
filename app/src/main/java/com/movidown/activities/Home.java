package com.movidown.activities;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.movidown.R;
import com.movidown.adapter.MovieListRecycleAdapter;
import com.movidown.data.remote.Requests;
import com.movidown.data.remote.ServiceRequests;
import com.movidown.domain.MovieDetail;
import com.movidown.interfaces.GetMovieDetailsCallback;
import com.movidown.interfaces.LoadNextPageCallback;
import com.movidown.utilities.PublicValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity implements GetMovieDetailsCallback{

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    MovieListRecycleAdapter movieListRecycleAdapter;
    LinearLayoutManager gridLayoutManager;


    private ArrayList<MovieDetail> movieArrayList = new ArrayList<>();
    int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        makeRequest();

    }

    private void bindData() {
        if (recyclerview != null) {

            //Layout Manager is initialized
            if (gridLayoutManager == null) {
                gridLayoutManager = new GridLayoutManager(this, 2);
            }
            //Setting Layout Manager
            if (gridLayoutManager != null && recyclerview != null) {
                recyclerview.setLayoutManager(gridLayoutManager);
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<MovieDetail> moviesArrayList, int nextPage) {

        pageNumber= nextPage;
        if (movieArrayList.size() >= PublicValues.NUM_ITEMS_IN_A_PAGE) {
            ArrayList<MovieDetail> tempMergeList = new ArrayList<>();
            tempMergeList = movieArrayList;

            for (int i = 0; i < moviesArrayList.size(); i++) {

                tempMergeList.add(moviesArrayList.get(i));
            }
            movieArrayList = tempMergeList;

            if (movieArrayList != null) {
                movieListRecycleAdapter.setsLoading(false);
                bindData();
                movieListRecycleAdapter.notifyDataSetChanged();
            }
        } else {
            //Occurs only in first time request
            movieArrayList = moviesArrayList;
            bindData();
            displayItemsWithPagination();
            //sets adapter for recyclerview
            recyclerview.setAdapter(movieListRecycleAdapter);
        }


    }

    public void displayItemsWithPagination() {

        movieListRecycleAdapter = new MovieListRecycleAdapter(movieArrayList, this, recyclerview);

        movieListRecycleAdapter.setOnLoadMore(new LoadNextPageCallback() {
            @Override
            public void onLoadMorePageItems() {

                if (movieArrayList.size() >= PublicValues.NUM_ITEMS_IN_A_PAGE) {

                    if (pageNumber != 0) {
                        //request for next page
                        makeRequest();
                    }
                }
            }
        });
    }

    private void makeRequest() {
        ServiceRequests.getInstance(Home.this).addToRequestQueue(Requests.getBlogComments(PublicValues.BASE_URL+pageNumber, this, pageNumber), "MOVIE DETAIL");
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onError(String error) {

    }
}
