package org.example.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.App;
import org.example.DayDAO;
import org.example.model.Day;
import org.example.model.Meal;

public class PrimaryController {

    @FXML
    private Button addMealButton;
    @FXML
    private Button viewMealButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button test;
    @FXML
    private ListView<Meal> mealList;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label calorieLabel;

    private Stage stage;

    @FXML
    public void initialize() throws IOException {
        DayDAO dao = DayDAO.getInstance();
        // disable dates later than the current date
        this.datePicker.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });
        // setValue of DatePicker to today's date when opening the first time, otherwise use dao to keep track of the
        // when switching back from another scene
        if(this.datePicker.getValue() == null) {
            this.datePicker.setValue(LocalDate.now());
        }

        if(dao.getFile() == null){
            dao.setFName("data.json");
            dao.setFile(new File(dao.getFName()));
            this.loadData(this.datePicker.getValue());
        }
    }

    @FXML
    public void openMealAdder(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/addMeal.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene addMealScene = new Scene(loader.load());
        stage.setScene(addMealScene);
        AddMealController addMealController = loader.getController();
        DayDAO dao = DayDAO.getInstance();
        addMealController.loadData(dao.get(this.datePicker.getValue()), this.datePicker.getValue());
    }

    @FXML
    public void openMealViewer(ActionEvent event) throws IOException {
        Meal meal = this.mealList.getSelectionModel().getSelectedItem();
        if(meal != null) {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/viewMeal.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene viewMealScene = new Scene(loader.load());
            ViewMealController viewMealController = loader.getController();
            stage.setScene(viewMealScene);
            viewMealController.loadData(meal, this.datePicker.getValue());
        }
    }

    @FXML
    public void deleteMeal(){
        DayDAO dao = DayDAO.getInstance();
        Day day = dao.get(this.datePicker.getValue());
        day.getMeals().remove(this.mealList.getSelectionModel().getSelectedItem());
        this.mealList.getItems().remove(this.mealList.getSelectionModel().getSelectedItem());
        if(this.mealList.getItems().isEmpty()){
            dao.delete(day);
        }else{
            dao.update(day);
        }
    }

    @FXML
    public void close(ActionEvent event){
        DayDAO dao = DayDAO.getInstance();
        dao.saveAll();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void pickDate(){
        this.loadData(this.datePicker.getValue());
    }

    // get data from json file and populate the meal ListView with that data
    public void loadData(LocalDate date){
        DayDAO dao = DayDAO.getInstance();
        Day day = dao.get(date);
        this.datePicker.setValue(date);
        if(day == null) {
            this.mealList.getItems().clear();
            this.calorieLabel.setText("0");
        }else{
            this.mealList.setItems(FXCollections.observableArrayList(day.getMeals()));
            this.calorieLabel.setText(String.valueOf(day.getCalories()));
        }
    }

    @FXML
    public void openChartViewer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/chartView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        try {
            Scene chartViewScene = new Scene(loader.load());
            stage.setScene(chartViewScene);
            ChartViewController chartViewController = loader.getController();
            chartViewController.loadData(LocalDate.now().minusWeeks(1), LocalDate.now());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
