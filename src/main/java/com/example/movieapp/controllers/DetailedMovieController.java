package com.example.movieapp.controllers;

import com.example.movieapp.Config;
import com.example.movieapp.MovieApplication;
import com.example.movieapp.models.movies.MovieResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailedMovieController implements Initializable {
    private int movieId;
    @FXML
    private Button siderHomeBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private MovieResponse movie;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.movieId = 0;
        this.movie = new MovieResponse();
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setMovie(MovieResponse movie) {
        this.movie = movie;
    }

    public void fetchMovie(int movieId) {
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

            // TODO: please write the method for displaying the movie detail here

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToHomeScene(ActionEvent event) {
        try {
            System.out.println("back to home page");

            FXMLLoader fxmlLoader = new FXMLLoader(MovieApplication.class.getResource("index.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
