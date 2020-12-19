package sample;

import agh.cs.projekt1.Grid;
import agh.cs.projekt1.SimulationMap;
import agh.cs.projekt1.SimulationScene;
import agh.cs.projekt1.Statistics;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import agh.cs.po.*;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {
    public Statistics stats;
    public static AtomicBoolean paused = new AtomicBoolean(false);
    private static final int PREFERRED_WIDTH = 600;
private static final int PREFERRED_HEIGHT = 600;
private javafx.scene.Scene scene;
private Pane root;

@Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        */
        Grid grid1 = new Grid(10, 10, 500, 500);
        agh.cs.projekt1.SimulationScene simScene = new SimulationScene(true, new Grid[]{grid1});
        stats = new Statistics();
        simScene.stats = stats;

        root = new Pane();

        javafx.scene.control.Button pause = new javafx.scene.control.Button("Pause");
        pause.setOnAction(event ->
        {
            if(!paused.get())
            {
                pause.setText("Start");
                paused.set(true);
            }
            else
            {
                pause.setText("Pause");
                paused.set(false);

                // Resume
                synchronized(simScene)
                {
                    simScene.notify();
                }
            }


        });
        root.getChildren().add(grid1.getTiles());
        root.getChildren().add(pause);
        //LineChart chart = new LineChart();
        scene = new javafx.scene.Scene(root);
        root.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MouseTest");
        primaryStage.show();
        simScene.start();

}


    public static void main(String[] args) {
        launch(args);
    }
}
