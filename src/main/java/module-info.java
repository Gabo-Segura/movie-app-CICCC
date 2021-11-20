module com.example.movieapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.movieapp to javafx.fxml;
    exports com.example.movieapp;
}