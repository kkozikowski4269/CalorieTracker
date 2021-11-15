package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrimaryController {

    @FXML
    private Button addMealButton;
    @FXML
    private Button closeButton;
    @FXML
    private ListView mealList;
    @FXML
    private DatePicker datePicker;

    private Stage stage;
    private Integer dayIndex = null;

    @FXML
    public void initialize(){

        if(this.datePicker.getValue() == null) {
            this.datePicker.setValue(LocalDate.now());
        }

        DayDAO dao = DayDAO.getInstance();

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
        SecondaryController secondaryController = loader.getController();
        secondaryController.setDate(this.datePicker.getValue().toString());
        secondaryController.setDayIndex(this.getDayIndex());
    }

    @FXML
    public void deleteMeal() throws IOException {
        return;
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
        }catch (IOException e){
            System.err.println("Could not set Data: " + e);
        }
    }

    // get data from json file and populate the meal ListView with that data
    private void loadData() throws IOException {
        DayDAO dao = DayDAO.getInstance();
        List<Day> days = dao.getAll();
        this.dayIndex = null;
        int i = 0;
        while(i < days.size() && dayIndex == null)
        {
            if(days.get(i).getDate().equals(this.datePicker.getValue().toString())){
                this.dayIndex = i;
            }
            i++;
        }
        if(this.dayIndex != null) {
            this.mealList.setItems(FXCollections.observableArrayList(days.get(this.dayIndex).getMeals()));
        }else{
            this.mealList.getItems().clear();
        }
    }

    public void setDate(int year, int month, int day){
        LocalDate date = LocalDate.of(year, month, day);
        this.datePicker.setValue(date);
    }

    public Integer getDayIndex(){
        return this.dayIndex;
    }
}
