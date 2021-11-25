package com.example.movieapp.components;

import com.example.movieapp.Config;
import com.example.movieapp.MovieApplication;
import com.example.movieapp.controllers.DetailedMovieController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    // change scene to navigate the details of a movie
    private void navigateMovieDetails(ActionEvent event) {
        try {
            System.out.println("Btn clicked.");
            Button btnClicked = (Button) event.getSource();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MovieApplication.class.getResource("detailed-movie.fxml"));

            DetailedMovieController controller = new DetailedMovieController();
            controller.fetchMovie(Integer.parseInt(btnClicked.getId()));
            fxmlLoader.setController(controller);

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
