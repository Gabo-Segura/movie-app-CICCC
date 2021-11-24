package com.example.movieapp.components;

import com.example.movieapp.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainMovieCard extends VBox {
    @FXML
    private ImageView moviePoster;
    @FXML
    private Text movieTitle;
    @FXML
    private MovieRating movieRating;
    @FXML
    private Button movieDetailNavBtn;

    public MainMovieCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-movie-card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.movieDetailNavBtn.setText("See Details");
            this.movieDetailNavBtn.setOnAction(this::navigateMovieDetails);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public MainMovieCard(String posterPath, String title, Double rating, int id) {
        this();
        this.setMoviePoster(posterPath);
        this.setMovieTitle(title);
        this.setMovieRating(rating);
        this.setBtnId(id);
    }

    public void setMoviePoster(String posterPath) {
        String url = Config.IMG_BASE_URL + "/w500" + posterPath;
        Image image = new Image(url);
        this.moviePoster.setImage(image);
        this.moviePoster.setPreserveRatio(true);
        this.moviePoster.setFitWidth(100);
    }

    public void setMovieTitle(String title) {
        this.movieTitle.setText(title);
    }

    public void setMovieRating(Double rating) {
        this.movieRating.setRating(String.valueOf(rating));
    }

    public void setBtnId(int id) {
        this.movieDetailNavBtn.setId(String.valueOf(id));
    }

    private void navigateMovieDetails(ActionEvent event) {
        // TODO: change screen
        System.out.println("Btn clicked.");
    }
}
