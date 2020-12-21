package agh.cs.projekt1;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import agh.cs.po.Map;
import agh.cs.po.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import sample.Main;

public class Statistics {
    private agh.cs.po.Map map;
    private int i = 0;
    private HBox box;
    private LineChart<Number,Number> chart;
    public int[] dominatingGenotype;
    private int[] mostDominatingGenotypeEver;
    private  int maxPossessedEver;
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series allAnimals = new XYChart.Series();
    private XYChart.Series allPlants = new XYChart.Series();
    private XYChart.Series domGenotype = new XYChart.Series();
    private XYChart.Series avgEnergy = new XYChart.Series();
    private XYChart.Series avgLifetime = new XYChart.Series();
    private XYChart.Series avgChildren = new XYChart.Series();
    private ArrayList animalC = new ArrayList();
    private ArrayList plantC = new ArrayList();
    private ArrayList energyC = new ArrayList();
    private ArrayList lifetimeC = new ArrayList();
    private ArrayList childrenC = new ArrayList();




    public Statistics(int width, int height)
    {
        box = new HBox();
        box.setMaxSize(width, height);
        box.setPrefSize(width, height);
        xAxis.setLabel("Dzień");
        chart = new LineChart<Number, Number>(xAxis,yAxis);
        chart.setPrefSize(width, height);
        xAxis.setStyle("-fx-font-color: #000000;");
        chart.setStyle(".line-chart-title {\n" +
                "-fx-text-fill: #FFFFFF;\n" +
                "-fx-fill: #FFFFFF;\n" +
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
        avgLifetime.setName("Srednia długość życia zmarłych w danym dniu");
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

    public void UpdateChart(SimulationScene scene)
    {

        ArrayList animals = map.GetAllWithTag("Animal");
        ArrayList dead = map.GetAllWithTag("dead");
        ArrayList plants = map.GetAllWithTag("Grass");
        int aliveCount = animals.size();
        int plantCount = plants.size();
        double averageEnergy = (double) animals.stream().collect(Collectors.averagingInt(x -> (((Animal)x).getEnergy())));
        double averageLifetime = (double) dead.stream().collect(Collectors.averagingInt(x -> (((Animal)x).lifetime)));
        double averageChildren = (double) animals.stream().collect(Collectors.averagingInt(x -> (((Animal)x).kids)));
        int day = ((SimulationScene)map.getScene()).day;
        allAnimals.getData().add(new XYChart.Data(day, aliveCount));
        animalC.add(aliveCount);
        allPlants.getData().add(new XYChart.Data(day, plantCount));
        plantC.add(plantCount);
        avgEnergy.getData().add(new XYChart.Data(day, averageEnergy));
        energyC.add(averageEnergy);
        avgLifetime.getData().add(new XYChart.Data(day, averageLifetime));
        lifetimeC.add(averageLifetime);
        avgChildren.getData().add(new XYChart.Data(day, averageChildren));
        childrenC.add(averageChildren);
        int maxPossesed = 0;
        synchronized (scene.existingGenoms)
        {
            for(Genom genes : scene.existingGenoms.values())
            {
                if(genes.getPossessedPawns().size() > maxPossesed && genes.getPossessedPawns().get(0).getMap()==map)
                {
                    maxPossesed = genes.getPossessedPawns().size();
                    dominatingGenotype = genes.genes;
                    if(maxPossesed > maxPossessedEver)
                    {
                        maxPossessedEver = maxPossesed;
                        mostDominatingGenotypeEver = dominatingGenotype;
                    }
                }
            }
        }
        chart.setTitle(Arrays.toString(dominatingGenotype) + " : " + maxPossesed);


    }

    public void SetChartTitle(String title)
    {
        chart.setTitle(title);
    }

    public void Export()
    {
        try {
            int day = ((SimulationScene)map.getScene()).day;
            int id = ((SimulationScene)map.getScene()).id;
            FileWriter myWriter = new FileWriter("M"+id+"D" + day + ".txt");
            myWriter.write("Srednia liczba zwierzat w ciagu dnia: " +
                    animalC.stream().collect(Collectors.averagingInt(obj -> (Integer) obj)) + "\n");
            myWriter.write("Srednia liczba roslin w ciagu dnia: " +
                    plantC.stream().collect(Collectors.averagingInt(obj -> (Integer) obj)) + "\n");
            myWriter.write("Srednia ilosc energii: " +
                    energyC.stream().collect(Collectors.averagingDouble(obj -> (double) obj)) + "\n");
            myWriter.write("Srednia liczba dzieci: " +
                    childrenC.stream().collect(Collectors.averagingDouble(obj -> (double) obj)) + "\n");
            myWriter.write("Sredni czas życia w dniach dla zmarłych: " +
                    lifetimeC.stream().collect(Collectors.averagingDouble(obj -> (double) obj)) + "\n");
            myWriter.write("Historycznie dominujący genom: " +
                    Arrays.toString(mostDominatingGenotypeEver));
            myWriter.close();
        }
        catch (IOException e)
        {
            Main.infoBox("An error occurred during saving to file.", "Error");
            e.printStackTrace();
        }
    }

    public synchronized void setI(int i) {
        this.i = i;
    }

    public synchronized int getI() {
        return i;
    }
}
