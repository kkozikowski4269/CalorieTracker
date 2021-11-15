package org.example;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class ViewMealController {
    @FXML
    TextField mealNameTextField;
    @FXML
    TextField itemNameTextField;
    @FXML
    TextField calorieTextField;

    @FXML
    ListView<FoodItem> foodList;

    @FXML
    Button addItemButton;
    @FXML
    Button removeItemButton;
    @FXML
    Button updateItemButton;
    @FXML
    Button updateMealButton;
    @FXML
    Button closeButton;

    @FXML
    VBox buttonVBox;

    private String date;

    public void initialize(){
        this.loadData();
    }

    @FXML
    public boolean addItem(){
        String itemName = itemNameTextField.getText().replace('-', ' ');
        String calories = calorieTextField.getText();
        Pattern pattern = Pattern.compile("0*[1-9][0-9]*");
        if(itemName.length() > 0 && calories.matches(pattern.pattern())) {
            foodList.getItems().add(new FoodItem(itemName, Integer.parseInt(calories)));
            itemNameTextField.clear();
            calorieTextField.clear();
            Collections.sort(foodList.getItems());
            foodList.getSelectionModel().clearSelection();
            return true;
        }
        return false;
    }

    @FXML
    public void removeItem(){
        FoodItem toRemove = foodList.getSelectionModel().getSelectedItem();
        foodList.getItems().remove(toRemove);
        foodList.getSelectionModel().clearSelection();
    }

    @FXML
    public void updateItem(){
        if(foodList.getSelectionModel().getSelectedItem() != null) {
            FoodItem foodItem = foodList.getSelectionModel().getSelectedItem();
            itemNameTextField.setText(foodItem.getName());
            calorieTextField.setText(String.valueOf(foodItem.getCalories()));
            foodList.setDisable(true);

            Button saveButton = new Button("Save");
            Button cancelButton = new Button("Cancel");
            saveButton.setStyle(addItemButton.getStyle());
            cancelButton.setStyle(addItemButton.getStyle());
            saveButton.setPrefWidth(addItemButton.getWidth());
            cancelButton.setPrefWidth(addItemButton.getWidth());
            saveButton.setOnAction(event -> save());
            cancelButton.setOnAction(event -> cancel());
            buttonVBox.getChildren().setAll(saveButton, cancelButton);
        }
    }

    @FXML
    public void updateMeal(ActionEvent event) throws IOException {
        Meal meal = PrimaryController.getCurrentMealSelection();
        meal.setFoodItems(new ArrayList<>(this.foodList.getItems()));
        DayDAO.getInstance().saveAll();
        this.close(event);
    }

    @FXML
    public void close(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    private void loadData(){
        Meal meal = PrimaryController.getCurrentMealSelection();
        this.mealNameTextField.setText(meal.getName());
        this.foodList.setItems(FXCollections.observableArrayList(meal.getFoodItems()));
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate() { return this.date; }

    // save the update to a food item
    private void save(){
        FoodItem selection = foodList.getSelectionModel().getSelectedItem();
        if(addItem()){
            foodList.getItems().remove(selection);
            cancel();
        }
    }

    // cancel the update to a food item
    private void cancel(){
        buttonVBox.getChildren().setAll(addItemButton,removeItemButton,updateItemButton,updateMealButton,closeButton);
        itemNameTextField.clear();
        calorieTextField.clear();
        foodList.getSelectionModel().clearSelection();
        foodList.setDisable(false);
    }
}
