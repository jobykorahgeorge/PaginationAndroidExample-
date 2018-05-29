package com.movidown.data.remote;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.movidown.domain.MovieDetail;
import com.movidown.interfaces.GetMovieDetailsCallback;
import com.movidown.utilities.PublicValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anooj on 20-Apr-18.
 */

public class Requests {
    public static JsonObjectRequest getBlogComments(String url, final GetMovieDetailsCallback getMovieDetailsCallback, final int page) {

        return new JsonObjectRequest( url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (isSuccess(response)) {
                        JSONObject dataJSON=response.getJSONObject("data");
                        ArrayList<MovieDetail> moviesArrayList = new ArrayList<>();
                        JSONArray movieJSONArray = dataJSON.getJSONArray("movies");

                        for (int i = 0; i < movieJSONArray.length(); i++) {

                            JSONObject movieJSON = movieJSONArray.getJSONObject(i);
                            MovieDetail movie = new MovieDetail();
                            movie.setMedium_cover_image(movieJSON.getString("medium_cover_image"));
                            movie.setTitle(movieJSON.getString("title"));
                            movie.setYear(movieJSON.getString("year"));
                            movie.setRating(movieJSON.getString("rating"));
                            moviesArrayList.add(movie);
                        }
                        if (movieJSONArray.length() > 0) {
                            if (movieJSONArray.length() == PublicValues.NUM_ITEMS_IN_A_PAGE) {//cheching any page left to load
                                //Callback with response and incremented page number
                                getMovieDetailsCallback.onSuccess(moviesArrayList, page + 1);
                            } else {
                                //Callback with response when no pae left to load.
                                getMovieDetailsCallback.onSuccess(moviesArrayList, 0);
                            }
                        }
                    }
                } catch (JSONException je) {
                    getMovieDetailsCallback.onError(je.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError)
                    getMovieDetailsCallback.onNetworkError();
                else
                    getMovieDetailsCallback.onError(error.getMessage());
            }
        }) {
            @Override

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                //setting request header
                map.put("Content-Type", "application/json");
                return map;
            }
        };


    }
    private static boolean isSuccess(JSONObject jsonObject) {
        try {
            //checking response status
            if (jsonObject.getString("status").equals("ok") && jsonObject.getString("status_message").equals("Query was successful")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
