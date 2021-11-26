package com.example.movieapp.components;

import com.example.movieapp.Config;
import com.example.movieapp.MovieApplication;
import com.example.movieapp.controllers.DetailedMovieController;
import com.example.movieapp.models.movies.MovieResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

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

    public MainMovieCard(String posterPath, String title, Double rating, int id, String side) {
        this();
        if (side.equals("right")) {
            this.setMoviePoster(posterPath, "thumbnail");
            this.setMovieTitle(title);
            this.setMovieRating(rating);
            this.setBtnId(id);
        }
    }

    public void setMoviePoster(String posterPath) {
        String url = Config.IMG_BASE_URL + "/w500" + posterPath;
        Image image = new Image(url);
        this.moviePoster.setImage(image);
        this.moviePoster.setPreserveRatio(true);
        this.moviePoster.setFitWidth(100);
    }

    public void setMoviePoster(String posterPath, String thumb) {
        String url = Config.IMG_BASE_URL + "/w500" + posterPath;
        Image image = new Image(url);
        this.moviePoster.setImage(image);
        this.moviePoster.setPreserveRatio(true);
        this.moviePoster.setFitWidth(75);
    }

    public ImageView getMoviePoster() {
        return moviePoster;
    }

    public void setMovieTitle(String title) {
        this.movieTitle.setText(title);
    }

    public Text getMovieTitle() {
        return movieTitle;
    }

    public void setMovieRating(Double rating) {
        this.movieRating.setRating(String.valueOf(rating));
    }

    public MovieRating getMovieRating() {
        return movieRating;
    }

    public void setBtnId(int id) {
        this.movieDetailNavBtn.setId(String.valueOf(id));
    }


    @FXML

    public Button getMovieDetailNavBtn() {
        return movieDetailNavBtn;
    }


    // change scene to navigate the details of a movie
    public void navigateMovieDetails(ActionEvent event) {
        try {
            Button btnClicked = (Button) event.getSource();

            FXMLLoader fxmlLoader = new FXMLLoader(MovieApplication.class.getResource("detailed-movie.fxml"));
            //Parent root = fxmlLoader.load();

            DetailedMovieController controller = fxmlLoader.getController();
            controller.fetchMovie(Integer.parseInt(btnClicked.getId()));
            fxmlLoader.setController(controller);


            Parent root = fxmlLoader.load();
            MovieResponse clickedMovie = controller.fetchMovie(Integer.parseInt(btnClicked.getId()));
            ImageView displayImage = (ImageView)fxmlLoader.getNamespace().get("movImage");
            displayImage.setImage(new Image(Config.IMG_BASE_URL + "/w500" + clickedMovie.getPosterPath()));

            Text displayTitle = (Text)fxmlLoader.getNamespace().get("movTitle");
            displayTitle.setText(clickedMovie.getTitle());

            Text displayPopularity = (Text)fxmlLoader.getNamespace().get("movPopularity");
            displayPopularity.setText(Double.toString(clickedMovie.getVoteAverage()));

            Text displayRelease = (Text)fxmlLoader.getNamespace().get("movRelease");
            displayRelease.setText(clickedMovie.getReleaseDate().toString());

            Text displayCountry = (Text)fxmlLoader.getNamespace().get("movCountry");
            displayCountry.setText("Country");

            Text displayLanguage = (Text)fxmlLoader.getNamespace().get("movLanguage");
            displayLanguage.setText("Language");

            Label displayOverview = (Label) fxmlLoader.getNamespace().get("movOverview");
            displayOverview.setText(clickedMovie.getOverview());
            displayOverview.setWrapText(true);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
