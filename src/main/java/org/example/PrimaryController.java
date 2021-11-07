package org.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private Button addMealButton;
    @FXML
    private Button closeButton;

    private Stage stage;

    @FXML
    public void initialize(){
        dayComboBox.setItems(FXCollections.observableArrayList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"));
        dayComboBox.setValue("Sunday");
    }

    @FXML
    public void openMealAdder(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addMeal.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene addMealScene = new Scene(loader.load());
        stage.setScene(addMealScene);
    }

    @FXML
    public void close(ActionEvent event){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
