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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TableCell;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML private TableView<Movie> tblMovies;
    @FXML private TableColumn<Movie, String> colMovieName;
    @FXML private TableColumn<Movie, String> colCategory;
    @FXML private TableColumn<Movie, Double> coIMDBRating;
    @FXML private TableColumn<Movie, Double> colPersonalRating;
    @FXML private TableColumn<Movie, LocalDateTime> colLastViewed;
    @FXML private Label welcomeText;
    private MovieModel movieModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            movieModel = new MovieModel();

            // Setup table columns
            setupTableColumns();

            // Bind the table to the observable list
            tblMovies.setItems(movieModel.getObservableMovies());


            refreshMovieList();

        } catch (Exception e) {
            showError("Initialization Error", "Failed to initialize application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        colMovieName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        coIMDBRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        colPersonalRating.setCellValueFactory(new PropertyValueFactory<>("personalRating"));
        colLastViewed.setCellValueFactory(new PropertyValueFactory<>("lastViewed"));

        // Format lastViewed column to proper date-time format
        colLastViewed.setCellFactory(column -> new TableCell<Movie, LocalDateTime>() {
            private final DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? "" : value.format(formatter));
            }
        });
    }

    private void refreshMovieList() {
        try {
            movieModel.loadAllMovies();
        } catch (Exception e) {
            showError("Error", "Failed to load movies: " + e.getMessage());
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
        refreshMovieList();
    }

    public void btnEditMovie(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            try {
                movieModel.deleteMovie(selectedMovie, false);
                refreshMovieList();
            } catch (Exception e) {
                showError("Delete Error", "Failed to delete movie: " + e.getMessage());
            }
        } else {
            showError("No Selection", "Please select a movie to delete");
        }
    }

    public void btnDeleteMovie(ActionEvent actionEvent) {

    }

    public void btnWatchMovie(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            try {
                // Update lastViewed to current time
                selectedMovie.setLastViewed(LocalDateTime.now());
                movieModel.updateMovie(selectedMovie);
                refreshMovieList();

                // TODO: Open media player with the movie file
                // movieModel.playMovie(selectedMovie);

            } catch (Exception e) {
                showError("Error", "Failed to open movie: " + e.getMessage());
            }
        } else {
            showError("No Selection", "Please select a movie to watch");
        }
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
}
