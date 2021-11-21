module com.example.movieapp {
    // for JavaFx
    requires javafx.controls;
    requires javafx.fxml;

//    // for HTTP
//    requires unirest.java;
//
//    // for JSON
//    requires gson;
//    requires java.sql;

    // for JavaFx
    opens com.example.movieapp to javafx.fxml;

    exports com.example.movieapp.components;
    exports com.example.movieapp;
}