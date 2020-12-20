package agh.cs.projekt1;

import java.util.ArrayList;

import agh.cs.po.Map;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

public class Statistics {
    private agh.cs.po.Map map;
    private int i = 0;
    private HBox box;
    private LineChart<Number,Number> chart;
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series allAnimals = new XYChart.Series();
    private XYChart.Series allPlants = new XYChart.Series();
    private XYChart.Series domGenotype = new XYChart.Series();
    private XYChart.Series avgEnergy = new XYChart.Series();
    private XYChart.Series avgLifetime = new XYChart.Series();
    private XYChart.Series avgChildren = new XYChart.Series();


    public Statistics(int width, int height)
    {
        box = new HBox();
        box.setMaxSize(width, height);
        xAxis.setLabel("Day");
        chart = new LineChart<Number, Number>(xAxis,yAxis);
        allAnimals.setName("Zwierzęta");
        allPlants.setName("Rośliny");
        chart.getData().add(allAnimals);
        chart.getData().add(allPlants);
        box.getChildren().add(chart);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public HBox getBox() {
        return box;
    }

    public void UpdateChart()
    {
        int alive = map.GetAllWithTag("Animal").size();
        int plant = map.GetAllWithTag("Grass").size();
        int day = ((SimulationScene)map.getScene()).day;
        allAnimals.getData().add(new XYChart.Data(day, alive));
        /*if(allAnimals.getData().size() > 30)
        {
            allAnimals.getData().remove(allAnimals.getData().get(0));
        }*/
        allPlants.getData().add(new XYChart.Data(day, plant));
        /*if(allPlants.getData().size() > 30)
        {
            allPlants.getData().remove(allPlants.getData().get(0));
        }*/
    }

    public synchronized void setI(int i) {
        this.i = i;
    }

    public synchronized int getI() {
        return i;
    }
}
