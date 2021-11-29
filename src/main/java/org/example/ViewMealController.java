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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class ViewMealController{
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

    private LocalDate date;
    private Meal meal;

    public void initialize(){
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
        this.meal.setName(this.mealNameTextField.getText());
        this.meal.setFoodItems(new ArrayList<>(this.foodList.getItems()));
        DayDAO dao = DayDAO.getInstance();
        dao.save(dao.get(this.date));
        this.close(event);
    }

    @FXML
    public void close(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        PrimaryController primaryController = loader.getController();
        primaryController.loadData(this.date);
    }

    public void loadData(Meal meal, LocalDate date){
        this.date = date;
        this.meal = meal;
        this.mealNameTextField.setText(meal.getName());
        this.foodList.setItems(FXCollections.observableArrayList(meal.getFoodItems()));
    }

    public LocalDate getDate() { return this.date; }

    // save the update to a food item
    private void save(){
        FoodItem selection = foodList.getSelectionModel().getSelectedItem();
        if(addItem()){
            foodList.getItems().remove(selection);
            this.cancel();
        }
    }

//     cancel the update to a food item
    private void cancel(){
        buttonVBox.getChildren().setAll(addItemButton,removeItemButton,updateItemButton,updateMealButton,closeButton);
        itemNameTextField.clear();
        calorieTextField.clear();
        foodList.getSelectionModel().clearSelection();
        foodList.setDisable(false);
    }
}
