package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class SetCalorieController {

    @FXML
    public void setCalories(){

    }

    @FXML
    public void cancel(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
