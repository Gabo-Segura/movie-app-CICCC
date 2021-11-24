package com.example.movieapp.components;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MovieRating extends HBox {
    @FXML
    private Text starIcon;
    @FXML
    private Text rating;

    public MovieRating() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("movie-rating.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            this.setIcon("\\{ starIcon \\}");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setIcon(String text) {
        iconTextProperty().set(text);
    }

    public StringProperty iconTextProperty() {
        return rating.textProperty();
    }


    public void setRating(String rating) {
        ratingTextProperty().set(rating);
    }

    public StringProperty ratingTextProperty() {
        return rating.textProperty();
    }
}
