package com.movidown.interfaces;

import com.movidown.domain.MovieDetail;

import java.util.ArrayList;

/**
 * Created by Anooj on 20-Apr-18.
 */

public interface GetMovieDetailsCallback {
    void onSuccess(ArrayList<MovieDetail> moviesArrayList, int pageNumber);
    void onNetworkError();
    void onError(String error);
}
