package com.example.MovieExam.GUI.Controller;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML private TableView<Movie> tblMovies;
    @FXML private TableColumn<Movie, String> colMovieName;
    @FXML private TableColumn<Movie, String> colCategory;
    @FXML private TableColumn<Movie, Double> coIMDBRating;
    @FXML private TableColumn<Movie, Double> colPersonalRating;
    @FXML private TableColumn<Movie, LocalDateTime> colLastViewed;
    @FXML private Label welcomeText;
    @FXML private Label lblDatabaseStatus;

    private MovieModel movieModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            movieModel = new MovieModel();

            // Setup table columns
            setupTableColumns();

            // Bind the table to the observable list
            tblMovies.setItems(movieModel.getObservableMovies());

            // Show database status
            updateDatabaseStatus();

            refreshMovieList();

        } catch (Exception e) {
            showError("Initialization Error", "Failed to initialize application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateDatabaseStatus() {
        if (lblDatabaseStatus != null) {
            if (DBConnector.isConnectionAvailable()) {
                lblDatabaseStatus.setText("Database: Connected ✓");
                lblDatabaseStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                lblDatabaseStatus.setText("Database: Offline (Read-only mode)");
                lblDatabaseStatus.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
            }
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
            updateDatabaseStatus();
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
        if (!DBConnector.isConnectionAvailable()) {
            showWarning("Database Offline",
                    "Cannot add movies while database is offline.\n\n" +
                            "Error: " + DBConnector.getLastError());
            return;
        }
        openMovieWindow(null);
        refreshMovieList();
    }

    public void btnEditMovie(ActionEvent actionEvent) {
        if (!DBConnector.isConnectionAvailable()) {
            showWarning("Database Offline",
                    "Cannot edit movies while database is offline.\n\n" +
                            "Error: " + DBConnector.getLastError());
            return;
        }

        Movie selected = tblMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a movie to edit");
            return;
        }
        openMovieWindow(selected);
        refreshMovieList();
    }

    public void btnDeleteMovie(ActionEvent actionEvent) {
        if (!DBConnector.isConnectionAvailable()) {
            showWarning("Database Offline",
                    "Cannot delete movies while database is offline.\n\n" +
                            "Error: " + DBConnector.getLastError());
            return;
        }

        Movie selected = tblMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a movie to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Movie");
        confirm.setContentText("Delete: " + selected.getName() + "?");

        ButtonType deleteFile = new ButtonType("Delete File Too");
        ButtonType deleteLibrary = new ButtonType("Library Only");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirm.getButtonTypes().setAll(deleteFile, deleteLibrary, cancel);

        Optional<ButtonType> result = confirm.showAndWait();

        try {
            if (result.isPresent() && result.get() == deleteFile) {
                movieModel.deleteMovie(selected, true);
                refreshMovieList();
            } else if (result.isPresent() && result.get() == deleteLibrary) {
                movieModel.deleteMovie(selected, false);
                refreshMovieList();
            }
        } catch (Exception ex) {
            showError("Error", "Failed to delete movie: " + ex.getMessage());
        }
    }

    public void btnWatchMovie(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie == null) {
            showWarning("No Selection", "Please select a movie to watch");
            return;
        }

        try {
            if (DBConnector.isConnectionAvailable()) {
                // Update lastViewed to current time
                selectedMovie.setLastViewed(LocalDateTime.now());
                movieModel.updateMovie(selectedMovie);
                refreshMovieList();
            } else {
                // In offline mode, just show a message
                showInfo("Offline Mode",
                        "Playing movie in offline mode.\n" +
                                "Last viewed date will not be updated until database is connected.");
            }

            // TODO: Open media player with the movie file
            // movieModel.playMovie(selectedMovie);

        } catch (Exception e) {
            showError("Error", "Failed to open movie: " + e.getMessage());
        }
    }

    public void openMovieWindow(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MovieExam/Views/MovieWindowView.fxml"));
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

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}