package org.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private Button addMealButton;
    @FXML
    private Button viewMealButton;
    @FXML
    private Button closeButton;
    @FXML
    private ListView<Meal> mealList;
    @FXML
    private DatePicker datePicker;

    private Stage stage;
    private Meal currentMealSelection = null;
    private Day currentDaySelection = null;
    private LocalDate currentDateSelection = null;

    @FXML
    public void initialize(){
        DayDAO dao = DayDAO.getInstance();

        // setValue of DatePicker to today's date when opening the first time, otherwise use dao to keep track of the
        // when switching back from another scene
        if(getDate() == null) {
            this.datePicker.setValue(LocalDate.now());
            setDate(datePicker.getValue());
        }else{
            this.datePicker.setValue(getDate());
        }

        if(dao.getFile() == null){
            dao.setFName("data.json");
            dao.setFile(new File(dao.getFName()));
        }

        try {
            this.loadData(this.getDate());
        }catch (IOException e){
            System.err.println("Could not set Data: " + e);
        }
    }

    @FXML
    public void openMealAdder(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addMeal.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene addMealScene = new Scene(loader.load());
        stage.setScene(addMealScene);
        AddMealController addMealController = loader.getController();
        addMealController.loadData(this.getCurrentDaySelection(), this.getDate());
    }

    @FXML
    public void openMealViewer(ActionEvent event) throws IOException {
        this.currentMealSelection = this.mealList.getSelectionModel().getSelectedItem();
        if(currentMealSelection != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewMeal.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene viewMealScene = new Scene(loader.load());
            ViewMealController viewMealController = loader.getController();
            stage.setScene(viewMealScene);
            viewMealController.loadData(this.getCurrentMealSelection(), this.getDate());
        }
    }

    @FXML
    public void deleteMeal(){
        this.currentMealSelection = this.mealList.getSelectionModel().getSelectedItem();
        System.out.println(this.currentMealSelection);
        DayDAO dao = DayDAO.getInstance();
        ArrayList<Day> days = dao.getAll();
        int index = days.indexOf(this.currentDaySelection);
        days.get(index).getMeals().remove(this.currentMealSelection);
        dao.saveAll();
        this.mealList.getItems().remove(this.currentMealSelection);
//        if(this.mealList.getItems().isEmpty()){
//            dao.delete(this.getCurrentDaySelection());
//        }
//        System.out.println(this.getCurrentDaySelection());
    }

    @FXML
    public void close(ActionEvent event){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void pickDate(){
        try {
            setDate(this.datePicker.getValue());
            this.loadData(this.getDate());
        }catch (IOException e){
            System.err.println("Could not set Data: " + e);
        }
    }

    // get data from json file and populate the meal ListView with that data
    private void loadData(LocalDate date) throws IOException {
        DayDAO dao = DayDAO.getInstance();
        Day d = new Day();
        d.setDate(date.toString());
        int dayIndex = dao.getAll().indexOf(d);
        if(dayIndex == -1) {
            currentDaySelection = null;
            this.mealList.getItems().clear();
        }else{
            currentDaySelection = dao.get(dayIndex);
            this.mealList.setItems(FXCollections.observableArrayList(currentDaySelection.getMeals()));
        }
    }

    public Meal getCurrentMealSelection(){
        return this.currentMealSelection;
    }

    public Day getCurrentDaySelection(){
        return this.currentDaySelection;
    }

    public void setDate(LocalDate date){
        this.currentDateSelection = date;
        this.datePicker.setValue(date);
    }

    public LocalDate getDate(){
        return this.currentDateSelection;
    }
}
