package org.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
    private static Meal currentMealSelection = null;
    private static Day currentDaySelection = null;
    private static LocalDate currentDateSelection = null;

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
            this.loadData();
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
    }

    @FXML
    public void openMealViewer(ActionEvent event) throws IOException {
        currentMealSelection = this.mealList.getSelectionModel().getSelectedItem();
        if(currentMealSelection != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewMeal.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene viewMealScene = new Scene(loader.load());
            ViewMealController viewMealController = loader.getController();
            stage.setScene(viewMealScene);
            viewMealController.setDate(this.datePicker.getValue().toString());

        }
    }

    @FXML
    public void deleteMeal() {

    }

    @FXML
    public void close(ActionEvent event){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void pickDate(){
        try {
            this.loadData();
            setDate(this.datePicker.getValue());
        }catch (IOException e){
            System.err.println("Could not set Data: " + e);
        }
    }

    // get data from json file and populate the meal ListView with that data
    private void loadData() throws IOException {
        DayDAO dao = DayDAO.getInstance();
        List<Day> days = dao.getAll();
        currentDaySelection = null;
        int i = 0;
        while(i < days.size() && currentDaySelection == null)
        {
            if(days.get(i).getDate().equals(this.datePicker.getValue().toString())){
                currentDaySelection = days.get(i);
            }
            i++;
        }
        if(currentDaySelection != null) {
            this.mealList.setItems(FXCollections.observableArrayList(currentDaySelection.getMeals()));
        }else{
            this.mealList.getItems().clear();
        }
    }

    public static Meal getCurrentMealSelection(){
        return currentMealSelection;
    }

    public static Day getCurrentDaySelection(){
        return currentDaySelection;
    }

    public static void setDate(LocalDate date){
        currentDateSelection = date;
    }

    public static LocalDate getDate(){
        return currentDateSelection;
    }
}
