package com.example.movieapp.controllers;

import com.example.movieapp.Config;
import com.example.movieapp.MovieApplication;
import com.example.movieapp.components.MainMovieCard;
import com.example.movieapp.models.movies.DiscoverMoviesResponse;
import com.example.movieapp.models.movies.MovieResponse;
import com.example.movieapp.models.movies.UpcomingMoviesResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    public Button homeBtn;
    @FXML
    private ImageView heroPoster;
    @FXML
    private Button navBtn;
    @FXML
    private VBox mainMoviesContainer;
    @FXML
    private Pagination pagination;
    @FXML
    private GridPane moviesContainer;
    @FXML
    private VBox popularWrapper;
    @FXML
    private VBox upcomingWrapper;

    private DiscoverMoviesResponse moviesResponse;
    private DiscoverMoviesResponse popularMoviesResponse;
    private UpcomingMoviesResponse upcomingMoviesResponse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.pagination == null) {
            this.pagination = new Pagination();
        }
        // initialize pagination
        this.pagination.setPageFactory(this::changePage);

        displayPopularMovies();
        displayUpcomingMovies();

        // concurrency
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                if (isCancelled()) {
                    updateMessage("Cancelled");
                }

                //Block the thread for a short time, but be sure
                //to check the InterruptedException for cancellation
                try {
                    Thread.sleep(500);
                    setHeroBackdrop();
                } catch (InterruptedException interrupted) {
                    if (isCancelled()) {
                        updateMessage("Cancelled");
                    }
                }
                return null;
            }
        };
        task.setOnSucceeded(workerStateEvent -> this.pagination.setPageCount(this.moviesResponse.getTotalPages()));

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void displayPopularMovies() {
        fetchMovies("popular", 1);
        for (int i = 0; i < 4; i++){
            MovieResponse movie = popularMoviesResponse.getMovies().get(i);

            String posterPath = movie.getPosterPath();
            String title = movie.getTitle();
            Double rating = movie.getVoteAverage();
            int id = movie.getId();

            HBox h1 = new HBox();
            HBox.setMargin(h1, new Insets(0,0,50,0));
            h1.setMinWidth(250.0);
            h1.setMinHeight(120.0);
            VBox v1 = new VBox();
            VBox v2 = new VBox();
            MainMovieCard popularMovieCard = new MainMovieCard(posterPath, title, rating, id, "right");
            v1.getChildren().add(popularMovieCard.getMoviePoster());
            Label titleLabel = new Label();
            titleLabel.setText(popularMovieCard.getMovieTitle().getText());
            titleLabel.setWrapText(true);
            v2.getChildren().add(titleLabel);
            v2.getChildren().add(popularMovieCard.getMovieRating());
            v2.getChildren().add(popularMovieCard.getMovieDetailNavBtn());
            h1.getChildren().add(v1);
            h1.getChildren().add(v2);
            popularWrapper.getChildren().add(h1);
        }
    }

    public void displayUpcomingMovies() {
        fetchMovies("upcoming", 1);
        for (int i = 0; i < 4; i++){
            MovieResponse movie = upcomingMoviesResponse.getMovies().get(i);
            System.out.println();
            String posterPath = movie.getPosterPath();
            String title = movie.getTitle();
            Double rating = movie.getVoteAverage();
            int id = movie.getId();

            HBox h1 = new HBox();
            HBox.setMargin(h1, new Insets(0,0,50,0));
            h1.setMinWidth(250.0);
            h1.setMinHeight(120.0);
            VBox v1 = new VBox();
            VBox v2 = new VBox();
            MainMovieCard upcomingMovieCard = new MainMovieCard(posterPath, title, rating, id, "right");
            v1.getChildren().add(upcomingMovieCard.getMoviePoster());
            Label titleLabel = new Label();
            titleLabel.setText(upcomingMovieCard.getMovieTitle().getText());
            titleLabel.setWrapText(true);
            v2.getChildren().add(titleLabel);
            v2.getChildren().add(upcomingMovieCard.getMovieRating());
            v2.getChildren().add(upcomingMovieCard.getMovieDetailNavBtn());
            h1.getChildren().add(v1);
            h1.getChildren().add(v2);
            upcomingWrapper.getChildren().add(h1);
        }
    }

    private void setHeroBackdrop() {
        String imgUrl = Config.IMG_BASE_URL + "/w500" + this.moviesResponse.getMovies().get(0).getBackdropPath();
        Image img = new Image(imgUrl);

        this.heroPoster.setImage(img);
    }

    private void setMoviesResponse(String type, JSONObject jsonObject) {
        int page = jsonObject.getInt("page");

        JSONArray results = jsonObject.getJSONArray("results");
        List<MovieResponse> movies = MovieResponse.parseMovies(results);

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
    public HttpResponse<JsonNode> getJsonRes(String directive, int pageNumber) throws UnirestException {
        String language = "en-US";
        String region = "US";
        String sortBy = "popularity.desc";
        String includeAdult = "false";
        String includeVideo = "false";
        String withWatchMonetizationTypes = "flatrate";
        String page = String.valueOf(pageNumber);

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

    public void fetchMovies(String type, int page) {
        try {
            System.out.println(type);
            HttpResponse<JsonNode> jsonRes;
            if (type.equals("popular")) {
                jsonRes = getJsonRes("/movie/popular", page);
            } else if (type.equals("upcoming")) {
                jsonRes = getJsonRes("/movie/upcoming", page);
            } else {
                jsonRes = getJsonRes("/discover/movie", page);
            }

            JSONObject jsonObject = jsonRes.getBody().getObject();
            setMoviesResponse(type, jsonObject);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private GridPane changePage(int index) {
        System.out.println(index);
        // fetch data from api
        fetchMovies("default", (index + 1));

        // initialize the movieContainer Section
        GridPane moviesContainer = new GridPane();
        moviesContainer.setHgap(20);
        moviesContainer.setVgap(20);
        moviesContainer.setAlignment(Pos.CENTER);
        addMovieCards(moviesContainer);

        return moviesContainer;
    }

    private void addMovieCards(GridPane moviesContainer) {
        int k = 0;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 3; col++) {
                MovieResponse movie = this.moviesResponse.getMovies().get(k);

                String posterPath = movie.getPosterPath();
                String title = movie.getTitle();
                Double rating = movie.getVoteAverage();
                int id = movie.getId();

                MainMovieCard mainMovieCard = new MainMovieCard(posterPath, title, rating, id);
                moviesContainer.add(mainMovieCard, col, row);
                k++;
            }
        }
    }

    @FXML
    public void navigateMovieDetails(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MovieApplication.class.getResource("detailed-movie.fxml"));
            Parent root = fxmlLoader.load();

            DetailedMovieController controller = fxmlLoader.getController();
            controller.fetchMovie(this.moviesResponse.getMovies().get(1).getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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