package com.example.movieapp.controllers;

import com.example.movieapp.Config;
import com.example.movieapp.components.MainMovieCard;
import com.example.movieapp.models.movies.DiscoverMoviesResponse;
import com.example.movieapp.models.movies.MovieResponse;
import com.example.movieapp.models.movies.UpcomingMoviesResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    public ImageView heroPoster;
    @FXML
    private GridPane moviesContainer;
    @FXML
    private VBox popularContainer;
    @FXML
    private VBox upcomingContainer;

    private DiscoverMoviesResponse moviesResponse;
    private DiscoverMoviesResponse popularMoviesResponse;
    private UpcomingMoviesResponse upcomingMoviesResponse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // fetch data from api
        fetchMovies("default");
        // display poster on Hero section
        setHeroBackdrop();
        // display main movies
        displayMovies();

        // TODO: display popular movies
        displayPopularMovies();
        // TODO: display upcoming movies
        displayUpcomingMovies();
    }

    // TODO: display popular movies
    private void displayPopularMovies() {
        fetchMovies("popular");
        for (int i = 0; i < 3; i++){
            MovieResponse movie = popularMoviesResponse.getMovies().get(i);

            String posterPath = movie.getPosterPath();
            String title = movie.getTitle();
            Double rating = movie.getVoteAverage();
            int id = movie.getId();

            MainMovieCard popularMovieCard = new MainMovieCard(posterPath, title, rating, id);
            this.popularContainer.getChildren().add(popularMovieCard);
        }
    }

    // TODO: display upcoming movies
    private void displayUpcomingMovies() {
        fetchMovies("upcoming");
        for (int i = 0; i < 3; i++){
            MovieResponse movie = upcomingMoviesResponse.getMovies().get(i);
            System.out.println();
            String posterPath = movie.getPosterPath();
            String title = movie.getTitle();
            Double rating = movie.getVoteAverage();
            int id = movie.getId();

            MainMovieCard upcomingMovieCard = new MainMovieCard(posterPath, title, rating, id);
            this.upcomingContainer.getChildren().add(upcomingMovieCard);
        }
    }

    private void setHeroBackdrop() {
        String imgUrl = Config.IMG_BASE_URL + "/w500" + this.moviesResponse.getMovies().get(0).getBackdropPath();
        Image img = new Image(imgUrl);

        this.heroPoster.setImage(img);
    }

    // display the fetched movie data on screen
    private void displayMovies() {
        int k = 0;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 3; col++) {
                MovieResponse movie = moviesResponse.getMovies().get(k);

                String posterPath = movie.getPosterPath();
                String title = movie.getTitle();
                Double rating = movie.getVoteAverage();
                int id = movie.getId();

                MainMovieCard mainMovieCard = new MainMovieCard(posterPath, title, rating, id);
                this.moviesContainer.add(mainMovieCard, col, row);
                k++;
            }
        }
    }

    private void setMoviesResponse(String type, JSONObject jsonObject) {
        int page = jsonObject.getInt("page");

        JSONArray results = jsonObject.getJSONArray("results");
        List<MovieResponse> movies = MovieResponse.parse(results);

        int totalPages = jsonObject.getInt("total_pages");
        int totalResults = jsonObject.getInt("total_results");

        DiscoverMoviesResponse moviesResponse = new DiscoverMoviesResponse(page, movies, totalPages, totalResults);

        switch (type) {
            case "default" -> {
                this.moviesResponse = moviesResponse;
                System.out.println("/discover/movie: " + this.moviesResponse);
            }
            case "popular" -> {
                this.popularMoviesResponse = moviesResponse;
                System.out.println("/movie/popular: " + this.popularMoviesResponse);
            }
            case "upcoming" -> {
                UpcomingMoviesResponse upcomingMoviesResponse = new UpcomingMoviesResponse(page, movies, totalPages, totalResults);
                JSONObject dates = jsonObject.getJSONObject("dates");
                upcomingMoviesResponse.setDates(dates);

                this.upcomingMoviesResponse = upcomingMoviesResponse;
                System.out.println("/movie/upcoming: " + this.upcomingMoviesResponse);
            }
        }
    }

    // https://developers.themoviedb.org/3/discover/movie-discover
    // TODO: change the query string to fetch different movies data
    public HttpResponse<JsonNode> getJsonRes(String directive) throws UnirestException {
        String language = "en-US";
        String region = "US";
        String sortBy = "popularity.desc";
        String includeAdult = "false";
        String includeVideo = "false";
        String withWatchMonetizationTypes = "flatrate";
        String page = "1";

        String url = Config.BASE_URL + directive;

        if (directive.equals("/movie/popular") || directive.equals("/movie/upcoming")) {
            return Unirest.get(url)
                .queryString("api_key", Config.API_KEY)
                .asJson();
        }
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

    public void fetchMovies(String type) {
        try {
            HttpResponse<JsonNode> jsonRes;
            if (type.equals("popular")) {
                jsonRes = getJsonRes("/movie/popular");
            } else if (type.equals("upcoming")) {
                jsonRes = getJsonRes("/movie/upcoming");
            } else {
                jsonRes = getJsonRes("/discover/movie");
            }

            JSONObject jsonObject = jsonRes.getBody().getObject();
            setMoviesResponse(type, jsonObject);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}