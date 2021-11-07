package org.example;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SecondaryController {
    @FXML
    private Button addItem;
    @FXML
    private TextField itemNameTextField;
    @FXML
    private TextField calorieTextField;
    @FXML
    private ListView<String> foodList;
    private Stage stage;

    @FXML
    private void addItem() throws IOException {
        String itemName = itemNameTextField.getText();
        String calories = calorieTextField.getText();
        if(itemName.length() > 0) {
            foodList.getItems().add(itemName + " " + calories);
            itemNameTextField.clear();
            calorieTextField.clear();
        }
    }

    @FXML
    private void removeItem(){
        String toRemove = foodList.getSelectionModel().getSelectedItem();
        foodList.getItems().remove(toRemove);
    }

    @FXML
    public void close(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}