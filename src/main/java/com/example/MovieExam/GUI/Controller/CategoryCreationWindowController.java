package com.example.MovieExam.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryCreationWindowController {
    @FXML
    private TextField txtCategory;


    public void btnCreateCtgClick(ActionEvent actionEvent){
      Stage stage = (Stage) txtCategory.getScene().getWindow();
      stage.close();
  }
}
