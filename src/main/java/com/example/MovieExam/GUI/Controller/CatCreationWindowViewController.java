package com.example.MovieExam.GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CatCreationWindowViewController {
    @FXML
    private TextField txtCategory;


    public void btnCreateCtgClick(ActionEvent actionEvent){
      Stage stage = (Stage) txtCategory.getScene().getWindow();
      stage.close();
  }
}
