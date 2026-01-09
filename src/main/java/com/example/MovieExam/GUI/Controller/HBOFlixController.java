package com.example.MovieExam.GUI.Controller;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.GUI.Model.MovieModel;
import com.example.MovieExam.Main;
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
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class HBOFlixController implements Initializable {
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


    public void btnAddNewC(ActionEvent actionEvent) throws IOException {
        openCategoryWindow(null);
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
        Movie selected = tblMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select a movie to edit");
            return;
        }
        openMovieWindow(selected);
    }

    public void btnDeleteMovie(ActionEvent actionEvent) {
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
            } else if (result.isPresent() && result.get() == deleteLibrary) {
                movieModel.deleteMovie(selected, false);
            }
        } catch (Exception ex) {
            showError("Error", "Failed to delete movie: " + ex.getMessage());
        }
    }

    public void btnWatchMovie(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            try {
                // Update lastViewed to current time
                selectedMovie.setLastViewed(LocalDateTime.now());
                movieModel.updateMovie(selectedMovie);
                refreshMovieList();

                // Open media player with the movie file
                openMediaWindow(selectedMovie);

            } catch (Exception e) {
                showError("Error", "Failed to open movie: " + e.getMessage());
            }
        } else {
            showError("No Selection", "Please select a movie to watch");
        }
    }

    public void openMediaWindow(Movie movie) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/MediaView.fxml"));
        Parent root = fxmlLoader.load();

        // Get the MediaController
        MediaController mediaController = fxmlLoader.getController();

        // Create and show the stage
        Stage stage = new Stage();
        stage.setTitle(movie != null ? "Playing: " + movie.getName() : "MediaView");
        stage.setScene(new Scene(root));
        stage.show();

        // Play the movie if one was selected
        if (movie != null && movie.getFileLink() != null) {
            mediaController.playVideo(movie.getFileLink());
            // Resize window to video dimensions once media is ready
            mediaController.setStageForResize(stage);
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

    public void openCategoryWindow (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/MovieExam/Views/CatCreationWindowView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Category Creation");
        stage.setScene(scene);
        stage.show();
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
    public void openMediaWindow(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/MediaView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("MediaView");
        stage.setScene(scene);
        stage.show();
    }
}
