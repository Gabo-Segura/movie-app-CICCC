package com.example.movieapp.models.movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpcomingMoviesResponse extends DiscoverMoviesResponse {
    List<String> dates;

    public UpcomingMoviesResponse(int page, List<MovieResponse> movies, int totalResults, int totalPages) {
        super(page, movies, totalResults, totalPages);
        this.dates = new ArrayList<>();
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(JSONObject json) {
        String maxDate = "";
        String minDate = "";
        try {
            maxDate = json.getString("maximum");
            minDate = json.getString("minimum");
        } catch (JSONException e) {
            System.out.println("missing field...");
        }

        this.dates.add(maxDate);
        this.dates.add(minDate);
    }
}
