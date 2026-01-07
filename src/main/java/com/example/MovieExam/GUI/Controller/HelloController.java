package com.example.MovieExam.GUI.Controller;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;
    private MovieModel movieModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            movieModel = new MovieModel();

        } catch (Exception e) {
            showError("Initialization Error", "Failed to initialize application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void btnAddNewC(ActionEvent actionEvent) {

    }

    public void btnEditC(ActionEvent actionEvent) {
    }

    public void btnDeleteC(ActionEvent actionEvent) {
    }

    public void btnNewMovie(ActionEvent actionEvent) {
        openMovieWindow(null);
    }

    public void btnEditMovie(ActionEvent actionEvent) {
    }

    public void btnDeleteMovie(ActionEvent actionEvent) {

    }

    public void btnWatchMovie(ActionEvent actionEvent) {
    }

    public void openMovieWindow(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MovieExam/Views/MovieWindowView.fxml")
            );
            Parent root = loader.load();

            MovieWindowViewController controller = loader.getController();
            controller.setMovieModel(movieModel);
            controller.setMovie(movie);

            Stage stage = new Stage();
            stage.setTitle(movie == null ? "New Movie" : "Edit Movie");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex) {
            showError("Error", "Failed to open movie window: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
