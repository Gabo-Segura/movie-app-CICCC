module com.example.movieapp {
    // for JavaFx
    requires javafx.controls;
    requires javafx.fxml;

    // for HTTP
    requires unirest.java;

    // for JSON
    requires json;

    // for JavaFx
    opens com.example.movieapp to javafx.fxml;

    exports com.example.movieapp;
    exports com.example.movieapp.controllers;
    exports com.example.movieapp.models.movies;
    opens com.example.movieapp.controllers to javafx.fxml;
    exports com.example.movieapp.components;
    opens com.example.movieapp.components to javafx.fxml;
}