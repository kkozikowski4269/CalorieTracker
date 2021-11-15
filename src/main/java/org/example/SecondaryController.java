package org.example;

import java.io.IOException;
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
import javafx.stage.Stage;

public class SecondaryController {
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

    private Button saveButton;
    private Button cancelButton;

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

    private String date;
    private Stage stage;
    private Integer dayIndex;

    @FXML
    private boolean addItem(){
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

            saveButton = new Button("Save");
            cancelButton = new Button("Cancel");
            saveButton.setStyle(addItemButton.getStyle());
            cancelButton.setStyle(addItemButton.getStyle());
            saveButton.setPrefWidth(addItemButton.getWidth());
            cancelButton.setPrefWidth(addItemButton.getWidth());
            saveButton.setOnAction(event -> save());
            cancelButton.setOnAction(event -> cancel());
            buttonVBox.getChildren().setAll(saveButton,cancelButton);
        }
    }

    @FXML
    private void addMeal(ActionEvent event){
        Meal meal = new Meal(this.mealNameTextField.getText());

        for(FoodItem i : foodList.getItems()) {
            meal.addItem(i);
        }
        DayDAO dao = DayDAO.getInstance();
        if(this.dayIndex != null) {
            dao.getAll().get(this.dayIndex).addMeal(meal);
        }else{
            Day day = new Day("Day");
            day.setDate(this.date);
            day.addMeal(meal);
            dao.getAll().add(day);
        }
        dao.saveAll();
        try {
            close(event);
        }catch (IOException e){
            System.err.println(e);
        }

    }

    @FXML
    public void close(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        PrimaryController primaryController = loader.getController();
        String[] dateDate = this.date.split("-");
        primaryController.setDate(Integer.parseInt(dateDate[0]),Integer.parseInt(dateDate[1]),Integer.parseInt(dateDate[2]));
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
    private void cancel(){
        buttonVBox.getChildren().setAll(addItemButton,removeItemButton,updateItemButton,addMealButton,closeButton);
        itemNameTextField.clear();
        calorieTextField.clear();
        foodList.getSelectionModel().clearSelection();
        foodList.setDisable(false);
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate() { return this.date; }

    public void setDayIndex(Integer i){
        this.dayIndex = i;
    }
}