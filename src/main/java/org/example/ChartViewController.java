package org.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ChartViewController {
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private Button viewButton;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    @FXML
    public void initialize(){
        this.startDate.setValue(LocalDate.now().minusWeeks(1));
        this.endDate.setValue(LocalDate.now());
        this.viewButton.setOnAction(e -> this.loadData(startDate.getValue(), endDate.getValue()));
        this.startDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });
        this.endDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });
    }

    public void loadData(LocalDate start, LocalDate end){
        if(end.compareTo(start) < 0){
            end = start;
            this.endDate.setValue(end);
        }
        this.viewButton.requestFocus();
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        List<Day> days = DayDAO.getInstance().getAll();
        while(start.compareTo(end) < 1){
            if(days.contains(new Day(start))){
                Day day = days.get(days.indexOf(new Day(start)));
                series.getData().add(new XYChart.Data<>(day.getDate(), day.getCalories()));
            }else{
                series.getData().add(new XYChart.Data<>(start.toString(), 0));
            }
            series2.getData().add(new XYChart.Data<>(start.toString(), 500));
            start=start.plusDays(1);
        }

        series.setName("Calories Consumed");
        series2.setName("Calorie Limit");
        lineChart.getData().addAll(series, series2);
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        PrimaryController primaryController = loader.getController();
        primaryController.setDate(LocalDate.now());
    }
}