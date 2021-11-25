package com.example.movieapp.controllers;

import com.example.movieapp.Config;
import com.example.movieapp.models.movies.MovieResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailedMovieController implements Initializable {
    @FXML
    private MovieResponse movie;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void fetchMovie(int movieId) {
        try {
            System.out.println("fetch movie.... " + movieId);

            String url = Config.BASE_URL + "/movie/" + movieId;
            HttpResponse<JsonNode> jsonRes =
                Unirest.get(url).queryString("api_key", Config.API_KEY).asJson();

            this.movie = MovieResponse.parse(jsonRes.getBody().getObject());
            System.out.println(this.movie.toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
