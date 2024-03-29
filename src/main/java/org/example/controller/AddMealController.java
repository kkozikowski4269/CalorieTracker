package org.example.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.App;
import org.example.DayDAO;
import org.example.model.Day;
import org.example.model.FoodItem;
import org.example.model.Meal;

public class AddMealController{
    @FXML
    private Button addItemButton;
    @FXML
    private Button removeItemButton;
    @FXML
    private Button updateItemButton;
    @FXML
    private Button addMealButton;
    @FXML
    private Button closeButton;

    @FXML
    private TextField mealNameTextField;
    @FXML
    private TextField itemNameTextField;
    @FXML
    private TextField calorieTextField;
    @FXML
    private ListView<FoodItem> foodList;

    @FXML
    private VBox buttonVBox;

    private Day day;
    private LocalDate date;

    @FXML
    private boolean addItem(){
        String itemName = itemNameTextField.getText().replace('-', ' ');
        String calories = calorieTextField.getText();
        if(itemName.length() > 0 && calories.matches("0*[1-9][0-9]*")) {
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
    private void removeItem(){
        FoodItem toRemove = foodList.getSelectionModel().getSelectedItem();
        foodList.getItems().remove(toRemove);
        foodList.getSelectionModel().clearSelection();
    }

    @FXML
    private void updateItem(){
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
            saveButton.setTextFill(Color.WHITE);
            cancelButton.setTextFill(Color.WHITE);
            saveButton.setOnAction(event -> save());
            cancelButton.setOnAction(event -> cancel());
            buttonVBox.getChildren().setAll(saveButton, cancelButton);
        }
    }

    @FXML
    private void addMeal(ActionEvent event) throws IOException {
        if(!this.mealNameTextField.getText().isEmpty() && !this.foodList.getItems().isEmpty()) {
            Meal meal = new Meal(this.mealNameTextField.getText());

            for (FoodItem i : foodList.getItems()) {
                meal.addItem(i);
            }
            DayDAO dao = DayDAO.getInstance();
            if (this.day != null) {
                this.day.addMeal(meal);
                dao.update(this.day);
            } else {
                this.day = new Day(this.date);
                this.day.setDate(this.date.toString());
                this.day.addMeal(meal);
                dao.save(this.day);
            }
            close(event);
        }
    }

    @FXML
    public void close(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/primary.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        PrimaryController primaryController = loader.getController();
        primaryController.loadData(this.date);
    }

    // save the update to a food item
    private void save(){
        FoodItem selection = foodList.getSelectionModel().getSelectedItem();
        if(addItem()){
            foodList.getItems().remove(selection);
            cancel();
        }
    }

    // cancel the update to a food item
    public void cancel(){
        buttonVBox.getChildren().setAll(addItemButton,removeItemButton,updateItemButton,addMealButton,closeButton);
        itemNameTextField.clear();
        calorieTextField.clear();
        foodList.getSelectionModel().clearSelection();
        foodList.setDisable(false);
    }

    public void loadData(Day day, LocalDate date){
        this.day = day;
        this.date = date;
    }
}