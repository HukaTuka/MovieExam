package com.example.MovieExam.GUI.Controller;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.GUI.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CatCreationWindowViewController implements Initializable {
    @FXML
    private TextField txtCategory;
    private CategoryModel categoryModel;
    private Category categoryToEdit;
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setCategoryModel(CategoryModel model) {
        this.categoryModel = model;
    }

    public void setCategory(Category category) {
        this.categoryToEdit = category;

        if (category != null) {
            txtCategory.setText(category.getName());

        }
    }

    private boolean allowedInput() {
        StringBuilder errors = new StringBuilder();

        if (txtCategory.getText().trim().isEmpty()) {
            errors.append("Category is required.\n");
        }
        if (errors.length() > 0) {
            return false;
        }

        return true;
    }


    public void btnCreateCtgClick(ActionEvent actionEvent) {
        if (allowedInput()) {
            try {
                String name = txtCategory.getText().trim();
                if (categoryToEdit != null) {
                    // Edit existing category
                    categoryToEdit.setName(name);

                    categoryModel.updateCategory(categoryToEdit);
                } else {
                    // Create new Category
                    categoryModel.createCategory(name);
                }
                saveClicked = true;
                closeWindow();

            } catch (Exception e) {

            }
        }
        closeWindow();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) txtCategory.getScene().getWindow();
        stage.close();
    }
}

