package com.example.MovieExam.GUI.Controller;

import com.example.MovieExam.GUI.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CatCreationWindowViewController implements Initializable {
    @FXML
    private TextField txtCategory;
    private Label lblFrick;
    private CategoryModel categoryModel;
    private boolean saveClicked = false;

    private static final String DATA_FOLDER = "data";
    private File dataDirectory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            categoryModel = new CategoryModel();

            dataDirectory = new File(DATA_FOLDER);

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
        lblFrick.setText(message);
        lblFrick.setVisible(true);
    }

    public void btnCreateCtgClick(ActionEvent actionEvent) {
        Stage stage = (Stage) txtCategory.getScene().getWindow();
        stage.close();
    }


    private boolean allowedInput() {
        StringBuilder errors = new StringBuilder();

        if (txtCategory.getText().trim().isEmpty()) {
            errors.append("Name is required.\n");
        }
        if (errors.length() > 0) {
            return false;
        }

        lblFrick.setVisible(false);
        return true;
    }



}