package sample;

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
    private static final int PREFERRED_WIDTH = 300;
private static final int PREFERRED_HEIGHT = 200;
private static final int RADIUS = 5;
private javafx.scene.Scene scene;
private Pane root;
private Circle selected;

    private Circle makeDot(double x, double y) {
        Circle dot = new Circle(x, y, RADIUS);
        dot.setOnMouseEntered(event ->
                scene.setCursor(Cursor.CROSSHAIR));
        dot.setOnMouseExited(event ->
                scene.setCursor(Cursor.DEFAULT));
        dot.setOnMouseDragged(event ->
        {
            dot.setCenterX(event.getX());
            dot.setCenterY(event.getY());
        });
        dot.setOnMousePressed(event ->
        {
            if (event.getClickCount() > 1) {
                root.getChildren().remove(selected);
                select(null);
            } else {
                select(dot);
            }
            event.consume();
        });
    dot.setOnKeyPressed(event ->
             {
         KeyCode code = event.getCode();
         int distance = event.isShiftDown() ? 10 : 1;
         if (code == KeyCode.DELETE)
             root.getChildren().remove(dot);
        else if (code == KeyCode.UP)
             dot.setCenterY(dot.getCenterY() - distance);
         else if (code == KeyCode.DOWN)
             dot.setCenterY(dot.getCenterY() + distance);
         else if (code == KeyCode.LEFT)
             dot.setCenterX(dot.getCenterX() - distance);
         else if (code == KeyCode.RIGHT)
             dot.setCenterX(dot.getCenterX() + distance);
         });

         return dot;
 }

    private void select(Circle dot) {
        if (selected == dot) return;
        if (selected != null) selected.setFill(Color.BLACK);
        selected = dot;
        if (selected != null) {
            selected.requestFocus();
            selected.setFill(Color.RED);
        }
    }

@Override
    public void start(Stage primaryStage) throws Exception{
        /*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        */
        agh.cs.projekt1.SimulationMap simMap = new SimulationMap(new Vector2d(10,10), 0.1f, true);
        agh.cs.projekt1.SimulationScene simScene = new SimulationScene(simMap, true);
        stats = new Statistics();
        simScene.stats = stats;

        root = new Pane();
        root.setOnMousePressed(event ->
        {
            double x = event.getX();
            double y = event.getY();
            Circle dot = makeDot(x, y);
            root.getChildren().add(dot);
            select(dot);
        });
        javafx.scene.control.Label label = new Label("0");
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
        root.getChildren().add(label);
        root.getChildren().add(pause);
        //LineChart chart = new LineChart();
        simScene.lab = label;
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
