package com.example.movieapp.controllers;

import com.example.movieapp.Config;
import com.example.movieapp.components.BootstrapPane;
import com.example.movieapp.models.movies.DiscoverMoviesResponse;
import com.example.movieapp.models.movies.MovieResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class IndexController {
    @FXML
    private BootstrapPane bootstrapLayout;

    // TODO: display the data to user
    private DiscoverMoviesResponse moviesResponse;

    public IndexController() {
        fetchMovies();
    }

    private void setMoviesResponse(JSONObject jsonObject) {
        int page = jsonObject.getInt("page");

        JSONArray results = jsonObject.getJSONArray("results");
        List<MovieResponse> movies = MovieResponse.parse(results);

        int totalPages = jsonObject.getInt("total_pages");
        int totalResults = jsonObject.getInt("total_results");

        this.moviesResponse = new DiscoverMoviesResponse(page, movies, totalPages, totalResults);
        System.out.println(this.moviesResponse);
    }

    // https://developers.themoviedb.org/3/discover/movie-discover
    // TODO: change the query string to fetch different movies data
    public HttpResponse<JsonNode> getJsonRes() throws UnirestException {
        String directive = "discover/movie";
        String language = "en-US";
        String region = "US";
        String sortBy = "popularity.desc";
        String includeAdult = "false";
        String includeVideo = "false";
        String withWatchMonetizationTypes = "flatrate";
        String page = "1";

        String url = Config.BASE_URL + directive;
        return Unirest.get(url)
            .queryString("api_key", Config.API_KEY)
            .queryString("language", language)
            .queryString("region", region)
            .queryString("sort_by", sortBy)
            .queryString("include_adult", includeAdult)
            .queryString("include_video", includeVideo)
            .queryString("with_watch_monetization_types", withWatchMonetizationTypes)
            .queryString("page", page)
            .asJson();
    }

    public void fetchMovies() {
        try {
            HttpResponse<JsonNode> jsonRes = getJsonRes();
            JSONObject jsonObject = jsonRes.getBody().getObject();

            setMoviesResponse(jsonObject);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}