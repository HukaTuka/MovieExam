package com.example.MovieExam.GUI.Controller;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.GUI.Model.MovieModel;

//Java imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
public class MovieWindowViewController implements Initializable {

    public Label lblName;
    public TextField txtName;
    public TextField txtImdbRating;
    public TextField txtPersonalRating;
    public TextField txtCategory;
    public TextField txtFileLink;
    public Label lblError;
    private MovieModel movieModel;
    private Movie movieToEdit;
    private boolean saveClicked = false;

    private static final String DATA_FOLDER = "data";
    private File dataDirectory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            movieModel = new MovieModel();

            // Initialize data directory
            dataDirectory = new File(DATA_FOLDER);

            // Create data folder if it doesn't exist
            if (!dataDirectory.exists()) {
                dataDirectory.mkdirs();
                System.out.println("Created data folder at: " + dataDirectory.getAbsolutePath());
            }

        } catch (IOException e) {
            showError("Failed to initialize: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }

    public void setMovieModel(MovieModel model) {
        this.movieModel = model;
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
    @FXML
    private void handleBrowse(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Movie File from Data Folder");

        // Set initial directory to data folder
        if (dataDirectory.exists()) {
            fileChooser.setInitialDirectory(dataDirectory);
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp4", "*.mpeg4"),
                new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"),
                new FileChooser.ExtensionFilter("MPEG4 Files", "*.mpeg4"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) txtFileLink.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Validate that file is in data folder
            if (isFileInDataFolder(selectedFile)) {
                // Store relative path from data folder
                String relativePath = getRelativePath(selectedFile);
                txtFileLink.setText(relativePath);

            } else {
                showError("Selected file must be in the 'data' folder!\nPlease copy your movie file to: " + dataDirectory.getAbsolutePath());
            }
        }
    }
    @FXML
    private void handleSave(ActionEvent event) {
        if (validateInput()) {
            try {
                String name = txtName.getText().trim();
                String category = txtCategory.getText().trim();
                Double imdbRating = Double.parseDouble(txtImdbRating.getText().trim());
                Double personalRating = Double.parseDouble(txtPersonalRating.getText().trim());
                String fileLink = txtFileLink.getText().trim();

                if (movieToEdit != null) {
                    // Edit existing movie
                    movieToEdit.setName(name);
                    movieToEdit.setCategory(category);
                    movieToEdit.setImdbRating(imdbRating);
                    movieToEdit.setPersonalRating(personalRating);
                    movieToEdit.setFileLink(fileLink);

                    movieModel.updateMovie(movieToEdit);
                } else {
                    // Create new movie
                    movieModel.createMovie(name, category, imdbRating, personalRating, fileLink);
                }

                saveClicked = true;
                closeWindow();


            } catch (Exception e) {
                showError("Error saving movie: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (txtName.getText().trim().isEmpty()) {
            errors.append("Name is required.\n");
        }
        if(txtCategory.getText().trim().isEmpty()) {
            errors.append("Category is required.\n");
        }
        if (txtImdbRating.getText().trim().isEmpty()) {
            errors.append("IMDB Rating is required.\n");
        } else {
            try {
                double imdbRating = Double.parseDouble(txtImdbRating.getText().trim());
                if (imdbRating < 0 || imdbRating > 10) {
                    errors.append("IMDB Rating must be between 0 and 10.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("IMDB Rating must be a valid number.\n");
            }
        }
        if(txtPersonalRating.getText().trim().isEmpty()) {
            errors.append("Personal Rating is required.\n");
        } else {
            try {
                double personalRating = Double.parseDouble(txtPersonalRating.getText().trim());
                if (personalRating < 0 || personalRating > 10) {
                    errors.append("Personal Rating must be between 0 and 10.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Personal Rating must be a valid number.\n");
            }
        }

        if (txtFileLink.getText().trim().isEmpty()) {
            errors.append("File path is required.\n");
        } else {
            // Validate file is in data folder
            File file = new File(txtFileLink.getText().trim());
            if (!file.exists()) {
                errors.append("Selected file does not exist.\n");
            } else if (!isFileInDataFolder(file)) {
                errors.append("File must be in the 'data' folder.\n");
            }
        }

        if (errors.length() > 0) {
            showError(errors.toString());
            return false;
        }

        lblError.setVisible(false);
        return true;
    }
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        saveClicked = false;
        closeWindow();
    }

    public void setMovie(Movie movie) {
        this.movieToEdit = movie;

        if (movie != null) {
            lblName.setText("Edit Movie");
            txtName.setText(movie.getName());
            txtCategory.setText(movie.getCategory());
            txtImdbRating.setText(String.valueOf(movie.getImdbRating()));
            txtPersonalRating.setText(String.valueOf(movie.getPersonalRating()));
            txtFileLink.setText(movie.getFileLink());

        } else {
            lblName.setText("New Movie");
        }
    }
private boolean isFileInDataFolder(File file) {
    try {
        String filePath = file.getCanonicalPath();
        String dataPath = dataDirectory.getCanonicalPath();
        return filePath.startsWith(dataPath);
    } catch (IOException e) {
        return false;
    }
}


private String getRelativePath(File file) {
    try {
        String filePath = file.getCanonicalPath();
        String dataPath = dataDirectory.getCanonicalPath();

        if (filePath.startsWith(dataPath)) {
            // Return path relative to project root (includes "data/")
            return Paths.get("").toAbsolutePath().relativize(file.toPath()).toString();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return file.getAbsolutePath();

}

}
