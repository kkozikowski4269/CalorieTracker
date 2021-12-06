package org.example.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    @FXML
    private AnchorPane anchorPane;

    private final String CALORIE_LIMIT = "src/main/resources/data_files/calorie_limit.txt";
    private final String DATA_FILE = "src/main/resources/data_files/data.json";

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
            File dataFile = new File(DATA_FILE);
            if(!dataFile.exists()){
                dataFile.createNewFile();
            }
            //dao.setFName(dataFile.getName());
            dao.setFile(dataFile);
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
        Integer calorieLimit = dao.getCalorieLimit(this.CALORIE_LIMIT);
        // if the calorie limit file is missing then create it and set the limit to 2000 by default
        if(calorieLimit == null){
            try {
                calorieLimit = 2000;
                dao.saveCalorieLimit(this.CALORIE_LIMIT,calorieLimit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int currentCalories = 0;
        if(day == null) {
            this.mealList.getItems().clear();
            this.calorieLabel.setText("0");
        }else{
            this.mealList.setItems(FXCollections.observableArrayList(day.getMeals()));
            this.calorieLabel.setText(String.valueOf(day.getCalories()));
            currentCalories = day.getCalories();
        }
        this.setCalorieWarningColor(calorieLimit,currentCalories);
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

    @FXML
    public void setDailyCalories(ActionEvent event){
        DayDAO dao = DayDAO.getInstance();

        for(Node n : this.anchorPane.getChildren()){
            n.setDisable(true);
        }
        VBox vBox = new VBox();
        HBox top = new HBox();
        HBox bottom = new HBox();

        top.setAlignment(Pos.CENTER);
        bottom.setAlignment(Pos.CENTER);

        Label label = new Label("Daily Calorie Limit:");
        TextField textField = new TextField();
        textField.setText(String.valueOf(dao.getCalorieLimit(CALORIE_LIMIT)));
        Button setButton = new Button("Set");
        Button cancelButton = new Button("Cancel");

        top.getChildren().addAll(label,textField);
        bottom.getChildren().addAll(setButton,cancelButton);

        vBox.setSpacing(10);
        top.setSpacing(5);
        bottom.setSpacing(10);

        vBox.setPadding(new Insets(10));
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(2))));
        vBox.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY, Insets.EMPTY)));
        vBox.setPrefWidth(300);
        vBox.setLayoutX((this.anchorPane.getWidth()/2)-(vBox.getPrefWidth()/2));
        vBox.setLayoutY(this.anchorPane.getHeight()/4);

        vBox.getChildren().addAll(top,bottom);
        this.anchorPane.getChildren().add(vBox);

        setButton.setOnAction(e -> {
            try {
                int calorieLimit = Integer.parseInt(textField.getText());
                dao.saveCalorieLimit(CALORIE_LIMIT,calorieLimit);
                int currentCals = Integer.parseInt(this.calorieLabel.getText());
                this.setCalorieWarningColor(calorieLimit,currentCals);
                cancelButton.fire();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        cancelButton.setOnAction(e -> {
            this.anchorPane.getChildren().remove(vBox);
            for(Node n : this.anchorPane.getChildren()){
                n.setDisable(false);
            }
        });
    }

    public void setCalorieWarningColor(int limit, int current){
        if(current > limit){
            this.calorieLabel.setTextFill(Color.RED);
        }else if((limit-current) <= limit*0.1){
            this.calorieLabel.setTextFill(Color.YELLOW);
        }else{
            this.calorieLabel.setTextFill(Color.WHITE);
        }
    }
}
