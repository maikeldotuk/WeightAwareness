/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weightawareness;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * @author Miguel Frias Mosquea
 */
public class WeightAwareness extends Application {

    private final double separator = 0.2;
    private final MeasuresCoord theSingleton = new MeasuresCoord();
    private Double minWeight, maxWeight, minBf, maxBf;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage myStage) {
        theSingleton.getLastTen();

        myStage.setTitle("WeightAwareness App");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 0, 0, 0));

        Text editWeightFormTitle = new Text("Add Today's Weight");
        editWeightFormTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(editWeightFormTitle, 0, 0, 2, 1);

        Label weightLabel = new Label("Weight:"), bodyFatLabel = new Label("Body Fat:"), dateLab = new Label("Date:");
        grid.add(weightLabel, 0, 1);
        grid.add(bodyFatLabel, 0, 2);
        grid.add(dateLab, 0, 3);

        TextField weightField = new TextField(), bodyfatField = new TextField(), dateField = new TextField();
        grid.add(weightField, 1, 1);
        grid.add(bodyfatField, 1, 2);
        grid.add(dateField, 1, 3);
        dateField.setDisable(true);

        Button btn = new Button("Add Weight");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbBtn2, 1, 5);

        //2nd scene
        calculateGraphEdges();

        final CategoryAxis xAxis = new CategoryAxis();
        //need to modify this to get the max and minimum there. 
        final NumberAxis yAxis = new NumberAxis(minWeight, maxWeight, 0.5);
        //xAxis.setLabel("Day");
        final LineChart<String, Number> lineChart
                = new LineChart<>(xAxis, yAxis);
        lineChart.getXAxis().setTickLabelsVisible(false);
        lineChart.getXAxis().setOpacity(0);
        lineChart.setStyle("CHART_COLOR_1: green;");

        lineChart.setTitle("Weight & Body Fat Monitoring");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Weight (kgs)");
        lineChart.setLegendVisible(false);

        //XYChart.Series series2 = new XYChart.Series();
        //series2.setName("Body Fat %");
        for (Measure aMeasure : theSingleton.getLastTen()) {
            series1.getData().add(new XYChart.Data(prettyDate(aMeasure), aMeasure.weight));
        }

        lineChart.getData().add(series1);

        //Second graph
        final CategoryAxis xAxis2 = new CategoryAxis();
        //need to modify this to get the max and minimum there. 
        final NumberAxis yAxis2 = new NumberAxis(minBf, maxBf, 0.5);
        xAxis2.setLabel("Day");
        final LineChart<String, Number> lineChart2
                = new LineChart<>(xAxis2, yAxis2);

        XYChart.Series series2 = new XYChart.Series();

        //XYChart.Series series2 = new XYChart.Series();
        series2.setName("Body Fat %");

        for (Measure aMeasure : theSingleton.getLastTen()) {

            series2.getData().add(new XYChart.Data(prettyDate(aMeasure), aMeasure.bodyFat));
        }

        //Just a test of Apache Commons toString builder.
        System.out.println(theSingleton.toString());

        lineChart2.getData().add(series2);
        lineChart2.setLegendVisible(false);
        //Ideally you want them one over the other one. 
        GridPane rootNode = new GridPane();

        rootNode.add(lineChart, 2, 0);
        rootNode.add(lineChart2, 2, 1);

        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        rootNode.add(separator1, 1, 0);
        rootNode.add(separator2, 1, 1);

        rootNode.add(grid, 0, 0);

//let's add here the listView
// I didn't quite understand why I have to make it from ArrayList to ObservableList. 
        ObservableList<Measure> oListOfMeasures = FXCollections.observableArrayList(theSingleton.getAll());
        ListView<Measure> theList = new ListView<>(oListOfMeasures);
        MultipleSelectionModel<Measure> lvSelModel = theList.getSelectionModel();

        lvSelModel.selectedItemProperty().addListener(
                (ObservableValue<? extends Measure> changed, Measure oldVal, Measure newVal) -> {
                    //System.out.println(newVal);
                    try {
                        weightField.setText(Double.toString(newVal.weight));
                        bodyfatField.setText(Double.toString(newVal.bodyFat));
                        dateField.setText(newVal.getFormattedDate());
                        btn.setText("Modify Weight");
                        editWeightFormTitle.setText("Modify Weight");

                    } catch (NullPointerException ae) {
                        //This exception is thrown because newVal is null when you click "modify weight".
                        System.out.println("No value from list selected");
                    }
                });


        GridPane gridList = new GridPane();
        gridList.setAlignment(Pos.CENTER);
        gridList.setHgap(10);
        gridList.setVgap(10);
        gridList.setPadding(new Insets(25, 10, 10, 10));


        Text scenetitle2 = new Text("Measurements");
        scenetitle2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridList.add(scenetitle2, 0, 0, 2, 1);
        gridList.add(theList, 0, 1, 2, 1);


        rootNode.add(gridList, 0, 1);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                try {
                    if (addWeight(weightField.getText(), bodyfatField.getText(), dateField.getText())) {

                        actiontarget.setText("Weight Modified");

//Modify graph for bf
                        series2.getData().clear();
                        for (Measure aMeasure : theSingleton.getLastTen()) {

                            series2.getData().add(new XYChart.Data(prettyDate(aMeasure), aMeasure.bodyFat));
                        }

                        //modify graph for weight:
                        series1.getData().clear();


                        for (Measure aMeasure : theSingleton.getLastTen()) {

                            series1.getData().add(new XYChart.Data(prettyDate(aMeasure), aMeasure.weight));
                        }

                        //Refresh max and min
                        calculateGraphEdges();
                        //yAxis = new NumberAxis(minWeight, maxWeight, 0.5);
                        yAxis.setLowerBound(minWeight);
                        yAxis.setUpperBound(maxWeight);

                        yAxis2.setLowerBound(minBf);
                        yAxis2.setUpperBound(maxBf);

//refresh the list
//oListOfMeasures = FXCollections.observableArrayList(theSingleton.getAll());
                        oListOfMeasures.removeAll(FXCollections.observableArrayList(theSingleton.getAll()));
                        oListOfMeasures.addAll(FXCollections.observableArrayList(theSingleton.getAll()));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Today's measure already added");
                        alert.setContentText("Wait until tomorrow to add another measure or modify it by choosing it in the Measurements table.");
                        alert.showAndWait();
                        //actiontarget.setText("Already weighted today");
                    }

                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Incorrect format typed");
                    alert.setContentText("You can only write a number, using the dot as separator of decimals, not text or empty fields");
                    alert.showAndWait();
                }
            }
        });

        Scene myScene2 = new Scene(rootNode, 700, 700);

        myStage.setScene(myScene2);
        myStage.show();
    }


    private boolean addWeight(String weight, String bodyfat, String date) {
        Double w = Double.parseDouble(weight);
        Double bf = Double.parseDouble(bodyfat);
        //Example of adding a measure, it takes the day of today.
        //MeasuresCoord sinton1 = sinton;
        return theSingleton.addMeasure(w, bf, date);

        // you need to make this error-proof. 
    }

    private void calculateGraphEdges() {

        minWeight = 200.0;
        maxWeight = 0.0;
        minBf = 200.00;
        maxBf = 0.0;
        for (Measure aMeasure : theSingleton.getLastTen()) {
            if (aMeasure.bodyFat > maxBf) {
                maxBf = aMeasure.bodyFat;
            }
            if (aMeasure.bodyFat < minBf) {
                minBf = aMeasure.bodyFat;
            }
            if (aMeasure.weight > maxWeight) {
                maxWeight = aMeasure.weight;
            }
            if (aMeasure.weight < minWeight) {
                minWeight = aMeasure.weight;
            }
        }
        //Add some extra to separate from top and bottom;
        minBf -= separator;
        maxBf += separator;
        minWeight -= separator;
        maxWeight += separator;
    }

    private String prettyDate(Measure aMeasure) {
        LocalDate todayDate = LocalDate.now();
        LocalDate yesterdayDate = todayDate.minusDays(1);
        String todaysDate = todayDate.format(Measure.aFormat);
        String yesterdaysDate = yesterdayDate.format(Measure.aFormat);
        String formatedCurrentDate = aMeasure.getFormattedDate();

        if (todaysDate.equals(formatedCurrentDate)) {
            return "Today";
        } else if (yesterdaysDate.equals(formatedCurrentDate)) {
            return "Yesterday";
        } else {
            return aMeasure.getFormattedDate();
        }

    }

}
