package agh.cs.projekt1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    private int maxPossesed = 0;
    public int[] dominatingGenotype;
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
        box.setPrefSize(width, height);
        xAxis.setLabel("Day");
        chart = new LineChart<Number, Number>(xAxis,yAxis);
        chart.setPrefSize(width, height);
        xAxis.setStyle("-fx-font-color: #000000;");
        chart.setStyle(".line-chart-title {\n" +
                "-fx-text-fill: #FFFFFF;\n" +
                "-fx-fill: #FFFFFF;\n" +
                "-fx-font-size: 1.6em;\n" +
                "}\n" +
                " \n" +
                ".axis {\n" +
                "    -fx-font-size: 1.4em;    \n" +
                "    -fx-tick-label-fill: white;\n" +
                "}\n"+
                ".axis-label {\n" +
                "  -fx-fill: #FFFFFF;\n" +
                "  -fx-font-color: #FFFFFF;\n" +
                "}");
        allAnimals.setName("Zwierzęta");
        allPlants.setName("Rośliny");
        avgEnergy.setName("Srednia energia");
        avgLifetime.setName("Srednia długość życia");
        avgChildren.setName("Srednia liczba dzieci");
        chart.getData().add(allAnimals);
        chart.getData().add(allPlants);
        chart.getData().add(avgEnergy);
        chart.getData().add(avgLifetime);
        chart.getData().add(avgChildren);
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

        ArrayList animals = map.GetAllWithTag("Animal");
        ArrayList plants = map.GetAllWithTag("Grass");
        int aliveCount = animals.size();
        int plantCount = plants.size();
        double averageEnergy = (double) animals.stream().collect(Collectors.averagingInt(x -> (((Animal)x).getEnergy())));
        double averageLifetime = (double) animals.stream().collect(Collectors.averagingInt(x -> (((Animal)x).lifetime)));
        double averageChildren = (double) animals.stream().collect(Collectors.averagingInt(x -> (((Animal)x).kids)));
        System.out.println(averageChildren);
        int day = ((SimulationScene)map.getScene()).day;
        allAnimals.getData().add(new XYChart.Data(day, aliveCount));
        allPlants.getData().add(new XYChart.Data(day, plantCount));
        avgEnergy.getData().add(new XYChart.Data(day, averageEnergy));
        avgLifetime.getData().add(new XYChart.Data(day, averageLifetime));
        avgChildren.getData().add(new XYChart.Data(day, averageChildren));
        synchronized (Genom.existingGenoms)
        {
            for(Genom genes : Genom.existingGenoms.values())
            {
                if(genes.getPossessedPawns().size() >= maxPossesed && genes.getPossessedPawns().get(0).getMap()==map)
                {
                    maxPossesed = genes.getPossessedPawns().size();
                    dominatingGenotype = genes.genes;
                }
            }
        }
        chart.setTitle(Arrays.toString(dominatingGenotype) + " : " + maxPossesed);


    }

    public void SetChartTitle(String title)
    {
        chart.setTitle(title);
    }

    public synchronized void setI(int i) {
        this.i = i;
    }

    public synchronized int getI() {
        return i;
    }
}
