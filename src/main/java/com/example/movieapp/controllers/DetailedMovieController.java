package com.example.movieapp.controllers;

import com.example.movieapp.Config;
import com.example.movieapp.models.movies.MovieResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailedMovieController extends IndexController implements Initializable  {
    private int movieId;
    @FXML
    private MovieResponse movie;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.movieId = 0;
        this.movie = new MovieResponse();
        displayPopularMovies();
        displayUpcomingMovies();

    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setMovie(MovieResponse movie) {
        this.movie = movie;
    }

    public MovieResponse fetchMovie(int movieId) {
        setMovieId(movieId);

        try {
            System.out.println("fetch movie.... " + movieId);

            String url = Config.BASE_URL + "/movie/" + movieId;
            HttpResponse<JsonNode> jsonRes =
                Unirest.get(url).queryString("api_key", Config.API_KEY).asJson();

            JSONObject jsonObj = jsonRes.getBody().getObject();
//            System.out.println(jsonObj);

            setMovie(MovieResponse.parse(jsonObj));
            System.out.println(this.movie);

            // TODO: please write the method for displaying the movie detail herer
            return movie;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
